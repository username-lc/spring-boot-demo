import cn.lc.springmybatisplus.bean.User;
import cn.lc.springmybatisplus.mapper.UserMapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author lc
 * @Date 2020/12/11/011 15:34
 * @Description
 */
public class TestMP {
    private ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");

    private UserMapper userMapper = ioc.getBean("userMapper", UserMapper.class);

    @Test
    public void testDataSourse() throws Exception {
        DataSource c3p0DataSource = ioc.getBean("c3p0DataSource", DataSource.class);
        System.out.println("c3p0DataSource:" + c3p0DataSource);

        Connection connection = c3p0DataSource.getConnection();
        System.out.println("connection:" + connection);
    }

    @Test
    public void getUser() {
        User user = userMapper.selectById(1);
        System.out.println("user:" + user);
    }

    @Test
    public void insertUser() {
        User user = new User();
        user.setName("lc");
        user.setAge(26);
        user.setEmail("124578@qq.com");
        user.setSalary(2000.0);
        Integer insert = userMapper.insert(user);
        System.out.println("insert:" + insert);
        //获取id
        System.out.println("key:" + user.getId());
    }

    @Test
    public void selectUser() {
        //1、通过id查询
        User user = userMapper.selectById(2);
        System.out.println(user);
        //2、通过条件查询
        User user1 = new User();
        user1.setName("Jack");
        User user2 = userMapper.selectOne(user1);
        System.out.println(user2);
        //3、通过多个id批量查询
        ArrayList<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        List<User> users = userMapper.selectBatchIds(list);
        System.out.println(users);
        //4、通过map封装条件查询
        HashMap<String, Object> map = new HashMap();
        map.put("name","lc");
        map.put("age",26);
        List<User> users1 = userMapper.selectByMap(map);
        System.out.println(users1);
        //5、分页查询
        List<User> users2 = userMapper.selectPage(new Page(2,2), null);
        System.out.println(users2);
    }
}
