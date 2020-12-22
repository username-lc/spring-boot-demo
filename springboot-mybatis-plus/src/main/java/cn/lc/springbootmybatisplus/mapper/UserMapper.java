package cn.lc.springbootmybatisplus.mapper;

import cn.lc.springbootmybatisplus.bean.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Service;

/**
 * @Author lc
 * @Date 2020/12/11/011 16:00
 * @Description 基于Mybatis 在mapper接口中编写CRUD相关的方法，提供mapper接口所对应的sql映射文件以及方法对应的sql语句
 * 基于MP     直接继承BaseMapper即可
 */
@Service
public interface UserMapper extends BaseMapper<User> {
}
