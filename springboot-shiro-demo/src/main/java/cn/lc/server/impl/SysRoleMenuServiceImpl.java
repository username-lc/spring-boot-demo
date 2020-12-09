package cn.lc.server.impl;

import cn.lc.bean.SysRoleMenuEntity;
import cn.lc.dao.SysRoleMenuDao;
import cn.lc.server.SysRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description 角色与权限业务实现
 * @Author lc
 * @Date 2020/10/13/013 15:57
 */
@Service("sysRoleMenuService")
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuDao, SysRoleMenuEntity> implements SysRoleMenuService {

}