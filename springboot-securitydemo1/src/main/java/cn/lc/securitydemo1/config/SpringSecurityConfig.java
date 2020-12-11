package cn.lc.securitydemo1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Author lc
 * @Date 2020/10/26/026 14:55
 * @Description 使用自定义  UserDetailsService 添加用户名密码
 * <p>
 * 第一步，创建配置类，设置使用哪个UserDetailsService实现类
 * 第二步，编写实现类MyUserDetailsService,返回User对象，User对象中有用户名密码和操作权限
 */

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.formLogin() //自定义登录表单
                .loginPage("/login.html") //自定义登录页面
                .loginProcessingUrl("/user/login") //自定义登录访问路径
                .defaultSuccessUrl("/test/index").permitAll() //登录成功后跳转的路径
                .and().authorizeRequests()
                .antMatchers("/","/test/hello","/user/login").permitAll() //设置 不需要认证的路径
                //1、hasAuthority方法  必须拥有admins的权限才能访问这个接口
                //.antMatchers("/test/index").hasAuthority("admins")
                //2、hasAnyAuthority  拥有admins或者manage任意的权限才能访问这个接口
                //.antMatchers("/test/index").hasAnyAuthority("admins,manage")
                //3、hasRole方法
                //.antMatchers("/test/index").hasRole("sale")
                //4、hasAnyRole方法
                .antMatchers("/test/index").hasAnyRole("sale","programmer")

                .anyRequest().authenticated()  //所有请求都可以访问
                .and().csrf().disable();  //关闭csrf防护

        //配置没有权限访问的页面
        http.exceptionHandling().accessDeniedPage("/unauth.html");
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
