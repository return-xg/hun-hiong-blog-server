package com.hunhiong.blog.service.impl;

import cn.hutool.core.util.IdUtil;
import com.hunhiong.blog.common.exception.BusinessException;
import com.hunhiong.blog.common.exception.ErrorCode;
import com.hunhiong.blog.config.FileUploadConfig;
import com.hunhiong.blog.entity.SysFile;
import com.hunhiong.blog.file.FileStorageService;
import com.hunhiong.blog.mapper.SysFileMapper;
import com.hunhiong.blog.service.FileService;
import com.hunhiong.blog.vo.FileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 文件上传服务实现
 *
 * @author hunhiong
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileStorageService fileStorageService;
    private final FileUploadConfig uploadConfig;
    private final SysFileMapper sysFileMapper;

    /** 日期目录格式 */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    @Override
    public FileVO upload(MultipartFile file) {
        // 校验文件是否为空
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAILED);
        }

        // 校验文件类型
        String contentType = file.getContentType();
        if (contentType == null || !uploadConfig.getAllowedTypes().contains(contentType)) {
            throw new BusinessException(ErrorCode.FILE_TYPE_NOT_ALLOWED);
        }

        // 校验文件大小
        if (file.getSize() > uploadConfig.getMaxSize()) {
            throw new BusinessException(ErrorCode.FILE_SIZE_EXCEEDED);
        }

        // 生成存储文件名：UUID + 原始扩展名
        String originalFilename = file.getOriginalFilename();
        String extension = getExtension(originalFilename);
        String storedName = IdUtil.fastSimpleUUID() + extension;

        // 生成相对路径：日期目录 / 文件名
        String dateDir = LocalDate.now().format(DATE_FORMATTER);
        String relativePath = dateDir + "/" + storedName;

        // 调用存储服务
        fileStorageService.store(file, relativePath);

        // 获取访问 URL
        String fileUrl = fileStorageService.getFileUrl(relativePath);

        // 保存文件记录到数据库
        SysFile sysFile = new SysFile();
        sysFile.setOriginalName(originalFilename);
        sysFile.setStoredName(storedName);
        sysFile.setStoragePath(relativePath);
        sysFile.setUrl(fileUrl);
        sysFile.setFileType(contentType);
        sysFile.setFileSize(file.getSize());
        sysFileMapper.insert(sysFile);

        log.info("文件上传成功: originalName={}, url={}", originalFilename, fileUrl);

        // 构建返回 VO
        FileVO vo = new FileVO();
        vo.setId(sysFile.getId());
        vo.setOriginalName(originalFilename);
        vo.setUrl(fileUrl);
        vo.setFileType(contentType);
        vo.setFileSize(file.getSize());
        return vo;
    }

    @Override
    public void delete(Long id) {
        SysFile sysFile = sysFileMapper.selectById(id);
        if (sysFile == null) {
            throw new BusinessException(ErrorCode.FILE_NOT_FOUND);
        }
        // MyBatis-Plus 全局已配置逻辑删除，deleteById 实际执行 UPDATE deleted=1
        sysFileMapper.deleteById(id);
        log.info("文件逻辑删除成功: id={}", id);
    }

    @Override
    public void batchDelete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        // MyBatis-Plus 全局已配置逻辑删除，deleteBatchIds 实际执行 UPDATE deleted=1
        sysFileMapper.deleteBatchIds(ids);
        log.info("批量逻辑删除文件成功: ids={}", ids);
    }

    /**
     * 获取文件扩展名（含点号）
     *
     * @param filename 文件名
     * @return 扩展名，如 .jpg
     */
    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}
