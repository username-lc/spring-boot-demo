package cn.lc.springbootmybatisplus.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

/**
 * @Author lc
 * @Date 2020/12/11/011 14:33
 * @Description
 */
@Data
@ToString
@TableName(value = "user")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "name")
    private String name;
    private Integer age;
    private String email;

    @TableField(exist = false)
    private Double salary;
}
