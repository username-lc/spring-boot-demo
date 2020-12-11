package cn.lc.securitydemo1.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sys_user")
public class Users implements Serializable {
    private Integer id;

    private String name;

    private String password;
}
