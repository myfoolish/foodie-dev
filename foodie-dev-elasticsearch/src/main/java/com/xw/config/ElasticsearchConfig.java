package com.xw.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author liuxiaowei
 * @Description
 * @date 2022/10/28
 */
@Configuration
public class ElasticsearchConfig {

    /**
     * 解决netty引起的issue
     */
    @PostConstruct  // 容器启动时进行初始化
    public void init() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }
}
