package cn.lc.common.util;


import cn.lc.common.model.APICode;
import cn.lc.common.model.APIResult;
import cn.lc.common.model.BaseCode;
import com.alibaba.fastjson.JSON;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tomas on 2017/6/4.
 */

public class HttpUtil {

    public  static List<RequestMatcher> ignoreRequest = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static void ok(HttpServletRequest request, HttpServletResponse response) {
        responseOutWithJson(request, response, APIResult.ok());
    }
    public static void error(HttpServletRequest request, HttpServletResponse response, BaseCode code) {
        if(code.getCode() == APICode._C_OK){
            responseOutWithJson(request,response, APIResult.ok());
        }else {
            responseOutWithJson(request,response, APIResult.error(code));
        }
    }
    public static void responseOutWithJson(HttpServletRequest request, HttpServletResponse response, Object data) {
        //将实体对象转换为JSON Object转换
        processHeader(request, response);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(JSON.toJSONString(data));
            logger.info("url= {} 返回JSON ={} ",request.getRequestURI(), JSON.toJSONString(data));
        } catch (IOException e) {
            logger.error("request ={} 返回 失败 e={}" ,request.getRequestURI(),e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public static void processHeader(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        String headers = request.getHeader("Access-Control-Request-Headers");
        if (!StringUtils.isEmpty(headers)) {
            headers = ", " + headers;
        } else {
            headers = "";
        }

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Content-Type", "application/json;charset=UTF-8");

        if (!StringUtils.isEmpty(origin)) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE, PATCH");
            response.setHeader("Access-Control-Allow-Headers", "Origin, X-Token, Authentication, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization, Cache-control "+headers);
            response.setHeader("Access-Control-Allow-Credentials", "true");
        }
    }

    public static void delCookies(HttpServletRequest request, HttpServletResponse response, String...  cookies) {
        for (String cookieName : cookies) {
            Cookie cookie = new Cookie(cookieName, null);
            String cookiePath = request.getContextPath();
            if (!StringUtils.hasLength(cookiePath)) {
                cookiePath = "/";
            }
            cookie.setPath(cookiePath);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    public static void antMatchers(HttpMethod httpMethod, String... antPatterns) {
        String method = httpMethod == null?null:httpMethod.toString();
        List<RequestMatcher> matchers = new ArrayList();
        String[] patterns = antPatterns;
        int length = antPatterns.length;

        for(int i = 0; i < length; ++i) {
            String pattern = patterns[i];
            matchers.add(new AntPathRequestMatcher(pattern, method));
        }
        ignoreRequest.addAll(matchers);
    }
    public static RequestMatcher[] getMatchersArray() {
        return ignoreRequest.toArray(new RequestMatcher[ignoreRequest.size()]);
    }


    /**
     * 获取请求basePath
     */
    public static String getBasePath(HttpServletRequest request) {
        StringBuffer basePath = new StringBuffer();
        String scheme = request.getScheme();
        String domain = request.getServerName();
        int port = request.getServerPort();
        basePath.append(scheme);
        basePath.append("://");
        basePath.append(domain);
        if("http".equalsIgnoreCase(scheme) && 80 != port) {
            basePath.append(":").append(String.valueOf(port));
        } else if("https".equalsIgnoreCase(scheme) && port != 443) {
            basePath.append(":").append(String.valueOf(port));
        }
        return basePath.toString();
    }


    /**
     * 获取请求主机  真实IP
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        String ip = request.getHeader("X-Forwarded-For");
        if (logger.isInfoEnabled()) {
            logger.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);
        }

        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            if (logger.isInfoEnabled()) {
                logger.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
            }
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            if (logger.isInfoEnabled()) {
                logger.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
            }
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            if (logger.isInfoEnabled()) {
                logger.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);
            }
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            if (logger.isInfoEnabled()) {
                logger.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);
            }
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (logger.isInfoEnabled()) {
                logger.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
            }
        }


        return ip.split(",")[0];
    }


    /**
     * 根据IP获取MAC地址
     * @param request
     * @return
     */
    public static String getMacAddrByIp(HttpServletRequest request) {
        String ip = getClientIp(request);
        String macAddr = null;
        try {
            Process process = Runtime.getRuntime().exec("nbtstat -a " + ip);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            Pattern pattern = Pattern.compile("([A-F0-9]{2}-){5}[A-F0-9]{2}");
            Matcher matcher;
            for (String strLine = br.readLine(); strLine != null;
                 strLine = br.readLine()) {
                matcher = pattern.matcher(strLine);
                if (matcher.find()) {
                    macAddr = matcher.group();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return macAddr;
    }

    /**
     * 获取系统版本信息
     * @param request
     * @return
     */
    public static String getSystemInfo(HttpServletRequest request){
        String systenInfo = null;
        String header = request.getHeader("user-agent");
        logger.info("获取系统版本信息:"+header);
        if(header == null || header.equals("")){
            return "";
        }


        //得到用户的操作系统
        if (header.indexOf("NT 6.0") > 0){
            systenInfo = "Windows Vista/Server 2008";
        } else if (header.indexOf("NT 5.2") > 0){
            systenInfo = "Windows Server 2003";
        } else if (header.indexOf("NT 5.1") > 0){
            systenInfo = "Windows XP";
        } else if (header.indexOf("NT 6.0") > 0){
            systenInfo = "Windows Vista";
        } else if (header.indexOf("NT 6.1") > 0){
            systenInfo = "Windows 7";
        } else if (header.indexOf("NT 6.2") > 0){
            systenInfo = "Windows Slate";
        } else if (header.indexOf("NT 6.3") > 0){
            systenInfo = "Windows 9";
        } else if (header.indexOf("NT 5") > 0){
            systenInfo = "Windows 2000";
        } else if (header.indexOf("NT 4") > 0){
            systenInfo = "Windows NT4";
        } else if (header.indexOf("Me") > 0){
            systenInfo = "Windows Me";
        } else if (header.indexOf("98") > 0){
            systenInfo = "Windows 98";
        } else if (header.indexOf("95") > 0){
            systenInfo = "Windows 95";
        } else if (header.indexOf("Mac") > 0){
            systenInfo = "Mac";
        } else if (header.indexOf("Unix") > 0){
            systenInfo = "UNIX";
        } else if (header.indexOf("Linux") > 0){
            systenInfo = "Linux";
        } else if (header.indexOf("SunOS") > 0){
            systenInfo = "SunOS";
        }
        return systenInfo;
    }

    public static UserAgent getUserAgent(String userAgentStr){
        return UserAgent.parseUserAgentString(userAgentStr);
    }

    /**
     * 获取设备类型
     * @param userAgentStr
     * @return
     */
    public static String getDeviceType(String userAgentStr) {
        if(StringUtils.isEmpty(userAgentStr)){
            return null;
        }

        UserAgent userAgent = getUserAgent(userAgentStr);

        //获取操作系统对象
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();

        //获取设备类型
        return operatingSystem.getDeviceType().toString();

    }

    /**
     * 获取厂商
     * @param userAgentStr
     * @return
     */
    public static String getDeviceMfrs(String userAgentStr) {
        if(StringUtils.isEmpty(userAgentStr)){
            return null;
        }

        UserAgent userAgent = getUserAgent(userAgentStr);

        //获取操作系统对象
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();

        //获取厂商
        return operatingSystem.getManufacturer().toString();

    }

    /**
     * 操作系统名称
     * @param userAgentStr
     * @return
     */
    public static String getDeviceName(String userAgentStr) {
        if(StringUtils.isEmpty(userAgentStr)){
            return null;
        }

        UserAgent userAgent = getUserAgent(userAgentStr);

        //获取操作系统对象
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();

        //操作系统名称
        return operatingSystem.getName();
    }

    /**
     * 获取设备类型
     * @param userAgentStr
     * @return
     */
    public static String getDeviceModel(String userAgentStr) {
        if(StringUtils.isEmpty(userAgentStr)){
            return null;
        }

        UserAgent userAgent = getUserAgent(userAgentStr);

        //获取操作系统对象
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        //操作系统名称
        String osName = operatingSystem.getName();
        //获取设备类型
        String deviceType = operatingSystem.getDeviceType().toString();

        //获取设备型号
        if ("MOBILE".equals(deviceType))
        {
            String split[] = userAgentStr.split("Build/");

            if(split!=null || split.length > 0)
            {
                String splitType[] =  split[0].split(";");
                if(splitType!=null && splitType.length>0)
                {
                    return splitType[splitType.length-1];
                }

            }

        }
        else if ("TABLET".equals(deviceType))
        {
            String split[] = userAgentStr.split("Build/");

            if(split!=null || split.length > 0)
            {
                String splitType[] =  split[0].split(";");
                if(splitType!=null && splitType.length>0)
                {
                    return splitType[splitType.length-1];
                }

            }
        }

        return osName;
    }
}
