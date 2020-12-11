package cn.lc.securitydemo1.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lc
 * @Date 2020/10/22/022 16:13
 * @Description
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public String add() {
        return "hello security";
    }

    @GetMapping("/index")
    public String index() {
        return "hello index";
    }

    @GetMapping("/update")
    //用户具有某个角色，可以访问方法
    @Secured({"ROLE_sale"})
    public String update() {
        return "hello update";
    }
}
