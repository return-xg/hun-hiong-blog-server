package com.hunhiong.blog;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 博客系统启动类
 *
 * @author hunhiong
 */
@Slf4j
@SpringBootApplication
@EnableScheduling
@MapperScan("com.hunhiong.blog.mapper")
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
        log.info("====== hun-hiong-blog-server 启动成功 ======");
    }
}
