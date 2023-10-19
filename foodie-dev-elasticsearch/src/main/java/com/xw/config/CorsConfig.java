package com.xw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author liuxiaowei
 * @Description
 * @date 2021/12/28
 */
@Configuration
public class CorsConfig {
    public CorsConfig() {
    }

    @Bean
    public CorsFilter corsFilter() {
        // 1、添加 cors 配置信息
        CorsConfiguration corsConfig = new CorsConfiguration();
        // 添加允许跨域的源
        corsConfig.addAllowedOrigin("http://localhost:63342");
        corsConfig.addAllowedOrigin("http://localhost:8088");
        corsConfig.addAllowedOrigin("http://localhost:8090");
        // 设置是否发送 cookie 信息
        corsConfig.setAllowCredentials(true);
        // 设置允许请求的方式
        corsConfig.addAllowedMethod("*");
        // 设置允许的 header
        corsConfig.addAllowedHeader("*");
        // 2、为 url 添加映射路径
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", corsConfig);

        //3、返回定义好的 corsSource
        return new CorsFilter(corsSource);
    }
}
