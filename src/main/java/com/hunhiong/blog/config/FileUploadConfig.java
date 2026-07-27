package com.hunhiong.blog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文件上传配置
 *
 * @author hunhiong
 */
@Data
@Component
@ConfigurationProperties(prefix = "file.upload")
public class FileUploadConfig {

    /** 本地存储根目录 */
    private String storagePath = "./uploads";

    /** 允许的文件类型（MIME） */
    private List<String> allowedTypes = List.of(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    /** 最大文件大小（字节）默认 5MB */
    private long maxSize = 5 * 1024 * 1024L;

    /** 文件访问 URL 前缀 */
    private String urlPrefix = "/uploads";
}
