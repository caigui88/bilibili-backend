package com.bilibili.notice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.bilibili.api.client")
@MapperScan(basePackages = {"com.bilibili.common.mapper","com.bilibili.notice.mapper"})
@ComponentScan(basePackages = "com.bilibili.*")
public class NoticeApplication {
    public static void main(String[] args) {
        SpringApplication.run(NoticeApplication.class,args);
    }
}
