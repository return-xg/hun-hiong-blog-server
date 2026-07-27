package com.hunhiong.blog.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 标签信息 VO
 *
 * <p>返回前端标签基本信息。</p>
 *
 * @author hunhiong
 */
@Data
public class TagVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 标签ID */
    private Long id;

    /** 标签名称 */
    private String name;

    /** 标签别名（URL友好） */
    private String slug;

    /** 创建时间 */
    private LocalDateTime createTime;
}
