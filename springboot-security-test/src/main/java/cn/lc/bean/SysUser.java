package cn.lc.bean;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class SysUser implements Serializable {

    private Integer id;

    private String name;

    private String password;
}
