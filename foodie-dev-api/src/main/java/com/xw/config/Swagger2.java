package com.xw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author liuxiaowei
 * @Description
 * @date 2021/12/28
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

    // http://localhost:8088/swagger-ui.html
    // http://localhost:8088/doc.html

    // 配置 swagger2 核心配置 docket
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)  // 指定 api 类型为 Swagger2
                .apiInfo(apiInfo()) // 用于定义 api 文档汇总信息
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.xw.controller"))  // 指定 controller
                .paths(PathSelectors.any()) // 所有 controller
                .build();


    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("foodie-dev 电商平台接口api")  //  文档描标题
                .contact(new Contact("xw",
                        "https://myfoolish.github.io/demo",
                        "myfoolish@126.com"))   // 联系人信息
                .description("foodie-dev 接口 api 文档")    // 详细信息
                .termsOfServiceUrl("开发中 待定")    // 网站地址
                .version("1.0")
                .build();
    }
}
