package cn.lc.server;

import cn.lc.bean.SysUserEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description 系统用户业务接口
 * @Author lc
 * @Date 2020/10/13/013 15:57
 */
public interface SysUserService extends IService<SysUserEntity> {

    /**
     * 根据用户名查询实体
     * @Author lc
     * @Date 2020/10/13/013 16:30
     * @Param  username 用户名
     * @Return SysUserEntity 用户实体
     */
    SysUserEntity selectUserByName(String username);

}

