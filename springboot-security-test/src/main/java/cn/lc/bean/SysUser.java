package cn.lc.bean;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class SysUser implements Serializable {

    /**
     * 序列化id
     */
    private static final long serialVersionUID = 4125096758084309L;

    private Integer id;

    private String name;

    private String password;
}
