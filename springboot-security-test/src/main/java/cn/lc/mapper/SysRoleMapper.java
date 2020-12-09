package cn.lc.mapper;

import cn.lc.bean.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysRoleMapper {
    @Select("SELECT * FROM sys_role WHERE id = #{id}")
    SysRole selectById(Integer id);
}
