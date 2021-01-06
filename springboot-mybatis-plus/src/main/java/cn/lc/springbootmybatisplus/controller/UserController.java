package cn.lc.springbootmybatisplus.controller;

import cn.lc.springbootmybatisplus.bean.User;
import cn.lc.springbootmybatisplus.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Wrapper;

/**
 * @Author lc
 * @Date 2020/12/11/011 15:58
 * @Description
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public User getUserById(@Param("id") String id) {
        return userMapper.selectById(id);
    }

    /**
     * AR查询操作   AR模式提供了一种更便捷的方式实现CRUD操作，其本质还是调用Mybatis的方法
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/ar")
    public User getUserByIdAR(@Param("id") String id) {
        User user = new User();
        User select = user.selectById(id);
        return select;
    }


    @GetMapping(value = "/page")
    public Page<User> page() {
        Page<User> page = new Page<>(1, 5);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");

        queryWrapper.like("name","t").or();
        queryWrapper.like("email","t");

        Page<User> userPage = userMapper.selectPage(page, queryWrapper);
        System.out.println("总页码："+page.getTotal());
        System.out.println("当前页码："+page.getCurrent());
        System.out.println("总页码："+page.getPages());
        System.out.println("每页显示的条数："+page.getSize());
        System.out.println("是否有上一页："+page.hasPrevious());
        System.out.println("是否有下一页："+page.hasNext());
        return userPage;
    }
}
