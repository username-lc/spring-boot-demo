package cn.lc.springmybatisplus.bean;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
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
    //@TableId(type = IdType.AUTO )
    private Integer id;
    @TableField(value = "name")
    private String name;
    private Integer age;
    private String email;

    @TableField(exist = false)
    private Double salary;
}
