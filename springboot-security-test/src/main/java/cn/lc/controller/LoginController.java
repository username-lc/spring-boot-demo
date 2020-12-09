package cn.lc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping("/")
    public String showHome() {
        //获取当前登录用户：SecurityContextHolder.getContext().getAuthentication()
//        String name = SecurityContextHolder.getContext().getAuthentication().getName();
//        logger.info("当前登陆用户：" + name);
        return "home";
    }

    @RequestMapping("/login")
    public String showLogin() {
        return "login";
    }

    //@PreAuthorize 用于判断用户是否有指定权限，没有就不能访问
    @RequestMapping("/admin")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String printAdmin() {
        return "如果你看见这句话，说明你有ROLE_ADMIN角色";
    }

    @RequestMapping("/user")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_USER')")
    public String printUser() {
        return "如果你看见这句话，说明你有ROLE_USER角色";
    }
}

