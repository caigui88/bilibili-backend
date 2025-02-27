package com.bilibili.search.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    @Bean(value = "dockerBean")
    public Docket dockerBean() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("搜索模块文档")
                        .description("# 关键字搜索得到匹配视频，获取历史搜索记录，搜索时得到匹配的关键字列表")
                        .termsOfServiceUrl("https://嘛.com/")
                        .contact("嘛")
                        .version("1.0")
                        .build())
                .groupName("搜索服务")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.bilibili.search.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}