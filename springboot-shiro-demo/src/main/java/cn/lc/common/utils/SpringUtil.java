package cn.lc.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author lc
 * @Date 2020/10/13/013 17:18
 * @Description spring上下文工具类
 */
@Component
public class SpringUtil implements ApplicationContextAware {
    private static ApplicationContext context;

    /**
     * Spring在bean初始化后会判断是不是ApplicationContextAware的子类
     * 如果该类是,setApplicationContext()方法,会将容器中ApplicationContext作为参数传入进去
     *
     * @Author lc
     * @CreateTime 2019/6/17 16:58
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 通过Name返回指定的Bean
     *
     * @Author lc
     * @CreateTime 2019/6/17 16:03
     */
    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }
}
