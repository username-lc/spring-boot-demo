package cn.lc.common.util;

/**
 * @Auther：csl
 * @date：2018/9/3
 * @Description：
 */
public class RedisKeyUtil
{
    //The key of access token into redis
    public static String WECHAT_ACCESS_TOKEN = "WC:AT:";

    public static String WECHAT_SESSION_KEY = "WC:SK:";

    public static String getRedisKey(String prefix, String key)
    {
        return String.format(prefix, key);
    }
}
