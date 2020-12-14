import cn.lc.springmybatisplus.bean.User;
import cn.lc.springmybatisplus.mapper.UserMapper;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @Author lc
 * @Date 2020/12/11/011 15:34
 * @Description
 */
public class TestMP {
    private ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");

   private UserMapper userMapper = ioc.getBean("userMapper",UserMapper.class);

    @Test
    public void testDataSourse() throws Exception{
        DataSource c3p0DataSource = ioc.getBean("c3p0DataSource", DataSource.class);
        System.out.println("c3p0DataSource:"+c3p0DataSource);

        Connection connection = c3p0DataSource.getConnection();
        System.out.println("connection:"+connection);
    }

    @Test
    public void getUser(){
        User user = userMapper.selectById(1);
        System.out.println("user:"+user);
    }

    @Test
    public void insertUser(){
        User user = new User();
        user.setName("lc");
        user.setAge(26);
        user.setEmail("124578@qq.com");
        user.setSalary(2000.0);
        Integer insert = userMapper.insert(user);
        System.out.println("insert:"+insert);
    }
}
