package cn.lc.common.handler;


import cn.lc.bean.SysUser;
import cn.lc.common.service.RedisService;
import cn.lc.common.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * redis处理者
 */
@Slf4j
@Component
public class RedisHandler {

    public final static String CMS_WEB_KEY = "web:cms:";

    public final static String USER_LOGIN_TOKEN_KEY = CMS_WEB_KEY + "token:%s";

    //缓存过期时间  1个月   单位秒
    public final long timeout = 1 * 24 * 60 * 60;

    @Autowired
    private RedisService redisService;

    @Scheduled(cron = "0/10 * * * * *")
    public void timer() {
        String result = (String) redisService.get("heartbeat");
        log.info("redis heartbeat !  beng  beng  beng ,redis status {}", result);
    }

    /**
     * redis缓存
     * 保存/刷新   token
     *
     * @param username
     * @param token
     */
    public void setToken(String username, String token) {
        String redisKey = RedisKeyUtil.getRedisKey(USER_LOGIN_TOKEN_KEY, username);
        redisService.set(redisKey, token, timeout);
    }

    /**
     * redis缓存
     * 获取   token
     *
     * @param username
     */
    public String getToken(String username) {
        String redisKey = RedisKeyUtil.getRedisKey(USER_LOGIN_TOKEN_KEY, username);
        return (String) redisService.get(redisKey);
    }

    /**
     * redis缓存
     * 删除   token
     *
     * @param username
     */
    public void deleteToken(String username) {
        String redisKey = RedisKeyUtil.getRedisKey(USER_LOGIN_TOKEN_KEY, username);
        redisService.delete(redisKey);
    }

    /**
     * redis缓存
     * 保存/刷新   USER
     *
     * @param username
     * @param sysUser
     */
    public void setUser(String username, SysUser sysUser) {
        String redisKey = RedisKeyUtil.getRedisKey("user:%s", username);
        redisService.set(redisKey, sysUser, timeout);
    }

}
