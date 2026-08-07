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

    /** 允许的图片类型（MIME） */
    private List<String> allowedTypes = List.of(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    /** 允许的音乐类型（MIME） */
    private List<String> allowedMusicTypes = List.of(
            "audio/mpeg", "audio/wav", "audio/mp4"
    );

    /** 图片最大文件大小（字节）默认 5MB */
    private long maxSize = 5 * 1024 * 1024L;

    /** 音乐最大文件大小（字节）默认 20MB */
    private long maxMusicSize = 20 * 1024 * 1024L;

    /** 文件访问 URL 前缀 */
    private String urlPrefix = "/uploads";
}
