package com.hunhiong.blog.file;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件存储服务接口（策略模式）
 *
 * <p>定义文件存储的通用契约，不同存储实现（本地、OSS 等）
 * 只需实现此接口即可无缝切换。</p>
 *
 * @author hunhiong
 */
public interface FileStorageService {

    /**
     * 存储文件
     *
     * @param file          上传的文件
     * @param relativePath  相对存储路径（如 2026/07/27/uuid.jpg）
     * @return 实际存储的相对路径
     */
    String store(MultipartFile file, String relativePath);

    /**
     * 获取文件访问 URL
     *
     * @param relativePath 相对存储路径
     * @return 文件访问完整 URL
     */
    String getFileUrl(String relativePath);
}
