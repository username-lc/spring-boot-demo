package cn.lc.common.shiro;

import cn.lc.common.constant.RedisConstant;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import java.io.Serializable;

/**
 * @Author lc
 * @Date 2020/10/13/013 17:24
 * @Description 自定义SessionId生成器
 */
public class ShiroSessionIdGenerator  implements SessionIdGenerator {
    /**
     * 实现SessionId生成
     * @Author lc
     * @CreateTime 2019/6/11 11:54
     */
    @Override
    public Serializable generateId(Session session) {
        Serializable sessionId = new JavaUuidSessionIdGenerator().generateId(session);
        return String.format(RedisConstant.REDIS_PREFIX_LOGIN, sessionId);
    }
}
