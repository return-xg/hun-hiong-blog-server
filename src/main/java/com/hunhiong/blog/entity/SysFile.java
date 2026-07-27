package com.hunhiong.blog.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统文件实体
 *
 * <p>记录上传文件的元信息，用于文件管理。</p>
 *
 * @author hunhiong
 */
@Data
@TableName("sys_file")
public class SysFile implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键ID（雪花算法） */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 原始文件名 */
    private String originalName;

    /** 存储文件名（UUID） */
    private String storedName;

    /** 存储相对路径 */
    private String storagePath;

    /** 文件访问URL */
    private String url;

    /** 文件类型（MIME） */
    private String fileType;

    /** 文件大小（字节） */
    private Long fileSize;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 逻辑删除：0-未删除，1-已删除 */
    private Integer deleted;
}
