package com.hunhiong.blog.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 博客分类实体
 *
 * <p>映射 blog_category 表，支持逻辑删除。</p>
 *
 * @author hunhiong
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("blog_category")
public class BlogCategory extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 分类名称 */
    private String name;

    /** 分类别名（URL友好） */
    private String slug;

    /** 排序（升序） */
    private Integer sort;

    /** 分类描述 */
    private String description;

    /** 逻辑删除：0-未删除，1-已删除 */
    @TableLogic
    private Integer deleted;
}
