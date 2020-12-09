package cn.lc.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author lc
 * @Date 2020/10/13/013 17:27
 * @Description 角色实体
 */
@Data
@TableName("sys_role")
public class SysRoleEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 角色ID
     */
    @TableId
    private Long roleId;
    /**
     * 角色名称
     */
    private String roleName;
}
