package com.hunhiong.blog.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置
 *
 * <p>配置文件上传静态资源映射，将 /uploads/** 请求映射到本地存储目录。</p>
 *
 * @author hunhiong
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final FileUploadConfig uploadConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将 /uploads/** 映射到本地存储目录
        String storagePath = uploadConfig.getStoragePath();
        // 确保路径以 / 结尾
        if (!storagePath.endsWith("/")) {
            storagePath += "/";
        }
        registry.addResourceHandler(uploadConfig.getUrlPrefix() + "/**")
                .addResourceLocations("file:" + storagePath);
    }
}
