package cn.lc.bean;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


@Data
@ToString
public class SysUserRole implements Serializable {
    static final long serialVersionUID = 1L;

    private Integer userId;

    private Integer roleId;
}
