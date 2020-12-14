package cn.lc.springmybatisplus.mapper;

import cn.lc.springmybatisplus.bean.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * @Author lc
 * @Date 2020/12/11/011 16:00
 * @Description 基于Mybatis 在mapper接口中编写CRUD相关的方法，提供mapper接口所对应的sql映射文件以及方法对应的sql语句
 * 基于MP     直接继承BaseMapper即可
 */
public interface UserMapper extends BaseMapper<User> {
}
