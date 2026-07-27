package com.hunhiong.blog.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件上传结果 VO
 *
 * @author hunhiong
 */
@Data
public class FileVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 文件ID */
    private Long id;

    /** 原始文件名 */
    private String originalName;

    /** 文件访问URL */
    private String url;

    /** 文件类型（MIME） */
    private String fileType;

    /** 文件大小（字节） */
    private Long fileSize;
}
