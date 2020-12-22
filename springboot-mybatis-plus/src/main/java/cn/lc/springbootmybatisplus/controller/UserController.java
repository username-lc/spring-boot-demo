package cn.lc.springbootmybatisplus.controller;

import cn.lc.springbootmybatisplus.bean.User;
import cn.lc.springbootmybatisplus.mapper.UserMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lc
 * @Date 2020/12/11/011 15:58
 * @Description
 */
@RestController(value = "/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public User getUserById(@Param("id") String id){
        return userMapper.selectById(id);
    }
}
