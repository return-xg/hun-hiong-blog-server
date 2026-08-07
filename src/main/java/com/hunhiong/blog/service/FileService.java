package com.hunhiong.blog.service;

import com.hunhiong.blog.common.enums.FileType;
import com.hunhiong.blog.vo.FileVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件上传服务接口
 *
 * @author hunhiong
 */
public interface FileService {

    /**
     * 上传文件（默认图片类型）
     *
     * @param file 上传的文件
     * @return 文件上传结果
     */
    FileVO upload(MultipartFile file);

    /**
     * 上传文件（指定类型）
     *
     * @param file     上传的文件
     * @param fileType 文件类型
     * @return 文件上传结果
     */
    FileVO upload(MultipartFile file, FileType fileType);

    /**
     * 删除文件（逻辑删除）
     *
     * @param id 文件ID
     */
    void delete(Long id);

    /**
     * 批量删除文件（逻辑删除）
     *
     * @param ids 文件ID列表
     */
    void batchDelete(List<Long> ids);
}
