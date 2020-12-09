package cn.lc.server.impl;

import cn.lc.bean.SysUserRoleEntity;
import cn.lc.dao.SysUserRoleDao;
import cn.lc.server.SysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description 用户与角色业务实现
 * @Author lc
 * @Date 2020/10/13/013 15:57
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao, SysUserRoleEntity> implements SysUserRoleService {

}