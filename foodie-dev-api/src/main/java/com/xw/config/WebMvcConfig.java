package com.xw.config;

import com.xw.controller.interceptor.UserTokenInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author liuxiaowei
 * @Description
 * @date 2022/4/8
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer  {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    // 实现静态资源对注册
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        WebMvcConfigurer.super.addResourceHandlers(registry);
        registry.addResourceHandler("/**")  // 映射所有对资源
                .addResourceLocations("classpath:/META-INF/resources/") // 映射 swagger2
                .addResourceLocations("file:/Users/liuxiaowei/downloads/"); // 映射本地静态资源，file 固定写法
    }

    @Bean
    public UserTokenInterceptor userTokenInterceptor() {
        return new UserTokenInterceptor();
    }

    /**
     * 注册拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userTokenInterceptor())
                .addPathPatterns("/hello")
                .addPathPatterns("/shopCart/add")
                .addPathPatterns("/shopCart/delete")
                .addPathPatterns("/address/list")
                .addPathPatterns("/address/add")
                .addPathPatterns("/address/update")
                .addPathPatterns("/address/delete")
                .addPathPatterns("/address/setDefault")
                .addPathPatterns("/orders/*")
                .excludePathPatterns("/orders/notifyMerchantOrderPaid")
                .addPathPatterns("/center/*")
                .addPathPatterns("/userInfo/*")
                .addPathPatterns("/myOrders/*")
                .excludePathPatterns("myOrders/deliver")
                .addPathPatterns("/myComments/*");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
