package cn.lc.common.config;

import cn.lc.common.handler.RedisHandler;
import cn.lc.security.CustomUserDetailsService;
import cn.lc.security.JwtTokenService;
import cn.lc.common.util.*;
import cn.lc.common.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private RedisHandler redisHandler;


    @Value("${jwt.header:X-Token}")
    private String tokenHeader;

    @Value("${jwt.cookie:X-Cookie}")
    private String cookieName;

    @Value("${jwt.expiration:604800}")
    private Integer expiration;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(charSequence.toString());
            }
        });
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 如果有允许匿名的url，填在下面
//                .antMatchers().permitAll()
                .anyRequest().authenticated()
                .and()
                // 设置登陆页
                .formLogin().loginPage("/login")
                .successHandler(new RestAuthenticationSuccessHandler())
                // 设置登陆成功页
                // 自定义登陆用户名和密码参数，默认为username和password
//                .usernameParameter("username")
//                .passwordParameter("password")
                .and()
                .logout().permitAll();

        // 关闭CSRF跨域
        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 设置拦截忽略文件夹，可以对静态资源放行
        web.ignoring().antMatchers("/css/**", "/js/**");
        HttpUtil.antMatchers(HttpMethod.OPTIONS);
        HttpUtil.antMatchers(HttpMethod.GET, "/error", "/login", "/", "/logout", "/favicon.ico");
        web.ignoring().requestMatchers(HttpUtil.getMatchersArray());
    }

    /**
     * Rest 登陆成功后的处理
     */
    public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request,
                                            HttpServletResponse response, Authentication authentication)
                throws ServletException, IOException {

            if (null != authentication.getPrincipal()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                //兼容 cookie 和 header x-token 验证两种方式
                String username = userDetails.getUsername();
                String generateToken = jwtTokenService.generateToken(username);

                redisHandler.setToken(username, generateToken);

                Cookie cookie = new Cookie(cookieName, generateToken);
                cookie.setPath("/");
                cookie.setMaxAge(expiration);
                cookie.isHttpOnly();
                response.addCookie(cookie);
                //重定向
                response.sendRedirect("/");
                response.addHeader(tokenHeader, jwtTokenService.generateToken(userDetails.getUsername()));
                //redisService.set(String.format(LOGIN_TOKEN_FORMAT_KEY,userDetails.getUsername()),userDetails,expiration, TimeUnit.SECONDS);
                HttpUtil.responseOutWithJson(request, response, APIResult.ok(userDetails));
                clearAuthenticationAttributes(request);

            }

        }
    }

}
