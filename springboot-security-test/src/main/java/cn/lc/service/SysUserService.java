package cn.lc.service;

import cn.lc.bean.SysUser;
import cn.lc.common.handler.RedisHandler;
import cn.lc.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class SysUserService {
    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private RedisHandler redisHandler;

    public SysUser selectById(Integer id) {
        return userMapper.selectById(id);
    }

    //@Cacheable(value = "user", cacheManager = "cacheManager", keyGenerator = "myKeyGenerator")
    public SysUser selectByName(String name) {
        SysUser sysUser = redisHandler.getUserHash(name);
        if (sysUser == null) {
            sysUser = userMapper.selectByName(name);
            //redisHandler.setUser(name,sysUser);
            redisHandler.setUserHash(name, sysUser);
        }
        return sysUser;
    }
}
