package cn.lc.security;


import cn.lc.common.util.HttpUtil;
import cn.lc.common.model.CmsWebAPICode;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private CustomUserDetailsService cmsUserDetailsService;


    @Value("${jwt.header:X-Token}")
    private String tokenHeader;

    @Value("${jwt.cookie:X-Cookie}")
    private String cookieName;

    @Value("${jwt.expiration:604800}")
    private Integer expiration;

    private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("StopWatch-StartTime");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        startTimeThreadLocal.set(System.currentTimeMillis());//线程绑定变量（该数据只有当前请求的线程可见）
        logger.info("进入JwtAuthenticationTokenFilter开始 url={} method={}", request.getRequestURI(), request.getMethod());
        if (HttpMethod.OPTIONS.matches(request.getMethod()) || request.getRequestURL().indexOf("favicon.ico") != -1) {
            logger.debug("OPTIONS 请求 忽略 返回200");
            response.setStatus(HttpStatus.OK.value());
            HttpUtil.ok(request, response);
            return;
        }
        //如果不想走过滤的url  忽略掉
        if (HttpUtil.ignoreRequest.size() > 0) {
            logger.debug("HttpUtil.ignoreRequest", JSON.toJSONString(HttpUtil.ignoreRequest));
            for (RequestMatcher matcher : HttpUtil.ignoreRequest) {
                logger.debug("matcher {}  isM {}", JSON.toJSONString(matcher), matcher.matches(request));
                if (matcher.matches(request)) {
                    chain.doFilter(request, response);
                    return;
                }
            }
        }


        String authToken = null;
        Cookie cookie = WebUtils.getCookie(request, this.cookieName);
        if (cookie != null && !StringUtils.isEmpty(cookie.getValue())) {
            authToken = cookie.getValue();
        } else if (!StringUtils.isEmpty(request.getHeader(this.tokenHeader))) {
            authToken = request.getHeader(this.tokenHeader);
        } else {
            logger.info("cookie认证失败，Token认证失败");
            HttpUtil.error(request, response, CmsWebAPICode.AUTHORIZED_FAILD);
            return;
        }

        //判断先解析 出 username
        logger.debug("checking authentication for cookie {} authToken {} ", JSON.toJSONString(cookie), authToken);
        String username = jwtTokenService.getUsernameFromToken(authToken);
        logger.debug("checking authentication for vo " + username);
        // 如果解析 username 为空 则重新登录
        if (username != null) {
            //判断当前用户token是否有效
           /* String currentToken = redisHandler.getToken(username);
            if(!authToken.equals(currentToken)){
                logger.debug("checking authentication for currentToken {} authToken {} ", currentToken, authToken);
                HttpUtil.error(request, response, CmsWebAPICode.AUTHORIZED_EXPIRE);
                return;
            }*/


            UserDetails userDetails = null;
            if (request.getSession().getAttribute("SPRING_SECURITY_CONTEXT_DETAILS") instanceof UserDetails) {
                userDetails = (UserDetails) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT_DETAILS");
                request.getSession().setMaxInactiveInterval(expiration);
            } else {

                userDetails = this.cmsUserDetailsService.loadUserByUsername(username);
                request.getSession().setMaxInactiveInterval(expiration);
                request.getSession().setAttribute("SPRING_SECURITY_CONTEXT_DETAILS", userDetails);
            }
            //验证 用户名和 token 的有效性
            if (jwtTokenService.validateToken(authToken, userDetails)) {
                //默认 cookie 一小时有效 但是我半小时 会更新一次 cookie 信息
                if ((System.currentTimeMillis() + expiration * 1000 / 2) > jwtTokenService.getExpirationDateFromToken(authToken).getTime()) {
                    authToken = jwtTokenService.generateToken(userDetails.getUsername());
                    cookie = new Cookie(cookieName, authToken);
                    cookie.setPath("/");
                    cookie.setMaxAge(expiration);
                    cookie.isHttpOnly();
                    response.addCookie(cookie);
                    //redisTemplate.opsForValue().set(String.format(LOGIN_TOKEN_FORMAT_KEY,username),userDetails,expiration, TimeUnit.SECONDS);
                }
                //正确的情况下 认证成功!
                chain.doFilter(request, response);
                logger.info("进入JwtAuthenticationTokenFilter结束 url={}", request.getRequestURI());
                long endTime = System.currentTimeMillis();
                long consumeTime = endTime - startTimeThreadLocal.get();
                if (consumeTime > 500) {
                    logger.info(" 请求: {} consume {} millis", request.getRequestURI(), consumeTime);
                }
                return;
            } else {
                logger.info(" 请求: {} token  失效 ,验证失败", request.getRequestURI());
                HttpUtil.error(request, response, CmsWebAPICode.INVALID_TOKEN);
                return;
            }
        } else {
            logger.info(" 请求: {} token  失效,没找到对应用户", request.getRequestURI());
            HttpUtil.error(request, response, CmsWebAPICode.INVALID_TOKEN);
            return;
        }

    }
}