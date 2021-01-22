package cn.lc.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * redis配置文件
 */
@Configuration
@EnableCaching
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 30)
public class RedisConfig extends CachingConfigurerSupport {
    Logger logger = LoggerFactory.getLogger(RedisConfig.class);
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.database}")
    private int database;

    @Value("${spring.jedis.pool.max-active}")
    private int maxActive;

    @Value("${spring.jedis.pool.max-wait}")
    private int maxWait;

    @Value("${spring.jedis.pool.min-idle}")
    private int minIdle;

    @Value("${spring.jedis.pool.max-idle}")
    private int maxIdle;


    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        logger.info("JedisPool注入成功！！");
        JedisPoolConfig config = new JedisPoolConfig();
        //最大连接数
        config.setMaxIdle(maxIdle);
        //最小空闲连接数
        config.setMinIdle(minIdle);
        //当池内没有可用的连接时，最大等待时间
        config.setMaxWaitMillis(maxWait);
        //总数
        config.setMaxTotal(maxActive);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        config.setTestWhileIdle(true);
        config.setNumTestsPerEvictionRun(10);
        config.setTimeBetweenEvictionRunsMillis(60000);
        return config;
    }

    /**
     * jedis连接工厂
     *
     * @param jedisPoolConfig
     * @return
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        //单机版jedis
        RedisStandaloneConfiguration redisStandaloneConfiguration =
                new RedisStandaloneConfiguration();
        //设置redis服务器的host或者ip地址
        redisStandaloneConfiguration.setHostName(host);
        //设置默认使用的数据库
        redisStandaloneConfiguration.setDatabase(database);
        //设置密码
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        //设置redis的服务的端口号
        redisStandaloneConfiguration.setPort(port);
        //获得默认的连接池构造器(怎么设计的，为什么不抽象出单独类，供用户使用呢)
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb =
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
        //指定jedisPoolConifig来修改默认的连接池构造器（真麻烦，滥用设计模式！）
        jpcb.poolConfig(jedisPoolConfig);
        //通过构造器来构造jedis客户端配置
        JedisClientConfiguration jedisClientConfiguration = jpcb.build();
        //单机配置 + 客户端配置 = jedis连接工厂
        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
    }


    /**
     * Jackson2JsonRedisSerializer和GenericJackson2JsonRedisSerializer的区别
     * https://blog.csdn.net/bai_bug/article/details/81222519
     */
    @Bean
    public Jackson2JsonRedisSerializer jackson2JsonRedisSerializer(ObjectMapper om) {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }


    /**
     * redis模板，存储关键字是字符串，值jackson2JsonRedisSerializer是序列化后的值
     *
     * @param
     * @return org.springframework.data.redis.core.RedisTemplate
     */
    @Bean(name = "redisTemplate")
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory factory, Jackson2JsonRedisSerializer jackson2JsonRedisSerializer) {
        RedisTemplate<?, ?> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        //使用StringRedisSerializer来序列化和反序列化redis的key值
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //key
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);

        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        //value
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.setDefaultSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;

    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory, Jackson2JsonRedisSerializer jackson2JsonRedisSerializer) {
        //生成一个默认配置 通过config对象即可对缓存进行自定义配置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        config.entryTtl(Duration.ofMinutes(1))     // 设置缓存的默认过期时间1小时，也是使用Duration设置
                .disableCachingNullValues();     // 不缓存空值

        //实例化key的序列化器对象
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        RedisSerializationContext.SerializationPair<String> pairKey = RedisSerializationContext.SerializationPair
                .fromSerializer(stringRedisSerializer);

        RedisSerializationContext.SerializationPair<Object> pairValue = RedisSerializationContext.SerializationPair
                .fromSerializer(jackson2JsonRedisSerializer);
        //序列化
        config.serializeKeysWith(pairKey);
        config.serializeValuesWith(pairValue);

        // 设置一个初始化的缓存空间set集合
        Set<String> cacheNames = new HashSet<>();
        cacheNames.add("my-redis-cache1");
        //cacheNames.add("my-redis-cache2");
        logger.info("RedisCacheConfiguration 配置信息：" + config.toString());
        // 对每个缓存空间应用不同的配置
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        configMap.put("my-redis-cache1", config);
        //configMap.put("my-redis-cache2", config.entryTtl(Duration.ofSeconds(120)));
        logger.info("RedisCacheManager 注入成功！");
        return RedisCacheManager.builder(factory)     // 使用自定义的缓存配置初始化一个cacheManager
                .cacheDefaults(config)
                .initialCacheNames(cacheNames)  // 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，再初始化相关的配置
                .withInitialCacheConfigurations(configMap)
                .build();
    }

    /**
     * 创建自定义密钥生成器。
     *
     * @return
     */
    @Bean
    public KeyGenerator myKeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(":").append(method.getName());
                for (Object obj : params) {
                    sb.append(":").append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

}
