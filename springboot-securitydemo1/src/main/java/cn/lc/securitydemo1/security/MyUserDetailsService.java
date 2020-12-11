package cn.lc.securitydemo1.security;

import cn.lc.securitydemo1.bean.Users;
import cn.lc.securitydemo1.mapper.UsersMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author lc
 * @Date 2020/10/28/028 15:57
 * @Description 实现UserDetailsService接口
 */
@Slf4j
@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //调用usersMapper方法 根据用户名查询数据库
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", username);
        Users users = usersMapper.selectOne(wrapper);
        if (users == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        log.info(users.toString());
        //给用户设置访问权限
        List<GrantedAuthority> role = AuthorityUtils.commaSeparatedStringToAuthorityList("admins,ROLE_sale");
        return new User(users.getName(), new BCryptPasswordEncoder().encode(users.getPassword()), role);
    }
}
