package com.hunhiong.blog.file;

import com.hunhiong.blog.common.exception.BusinessException;
import com.hunhiong.blog.common.exception.ErrorCode;
import com.hunhiong.blog.config.FileUploadConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * 本地文件存储服务实现
 *
 * <p>将文件存储到本地磁盘，按日期目录组织文件。</p>
 *
 * @author hunhiong
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "file.upload.storage-type", havingValue = "local", matchIfMissing = true)
public class LocalStorageService implements FileStorageService {

    private final FileUploadConfig uploadConfig;

    @Override
    public String store(MultipartFile file, String relativePath) {
        try {
            // 构建完整存储路径
            Path fullPath = getFullPath(relativePath);
            // 确保目录存在
            Files.createDirectories(fullPath.getParent());
            // 写入文件
            Files.copy(file.getInputStream(), fullPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("文件存储成功: {}", relativePath);
            return relativePath;
        } catch (IOException e) {
            log.error("文件存储失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    @Override
    public String getFileUrl(String relativePath) {
        // 拼接 URL 前缀与相对路径
        String prefix = uploadConfig.getUrlPrefix();
        if (!prefix.endsWith("/")) {
            prefix += "/";
        }
        return prefix + relativePath;
    }

    /**
     * 获取文件完整存储路径
     *
     * @param relativePath 相对路径
     * @return 完整路径
     */
    private Path getFullPath(String relativePath) {
        return Paths.get(uploadConfig.getStoragePath()).resolve(relativePath);
    }
}
