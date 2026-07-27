package com.hunhiong.blog.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 分类信息 VO
 *
 * <p>返回前端分类基本信息。</p>
 *
 * @author hunhiong
 */
@Data
public class CategoryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 分类ID */
    private Long id;

    /** 分类名称 */
    private String name;

    /** 分类别名（URL友好） */
    private String slug;

    /** 排序（升序） */
    private Integer sort;

    /** 分类描述 */
    private String description;

    /** 创建时间 */
    private LocalDateTime createTime;
}
