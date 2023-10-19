package com.xw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author liuxiaowei
 * @Description
 * @date 2021/7/21
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
// 扫描 mybatis 通用 mapper 所在包
@MapperScan(basePackages = "com.xw.mapper")
// 扫描所有包 以及相关组件包
@ComponentScan(basePackages = {"com.xw","org.n3r.idworker"})
@EnableScheduling   // 开启定时任务
@EnableRedisHttpSession //开启使用 redis 作为 spring session
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
