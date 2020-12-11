package cn.lc.securitydemo1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@MapperScan(basePackages = {"cn.lc.securitydemo1.mapper"})
//开启权限控制注解
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SpringbootSecurityDemo1Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootSecurityDemo1Application.class, args);
    }

}
