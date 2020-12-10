package com.lc.session01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

//@EnableRedisHttpSession
@SpringBootApplication
public class Session02Application {

    public static void main(String[] args) {
        SpringApplication.run(Session02Application.class, args);
    }

}
