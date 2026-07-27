package com.hunhiong.blog.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 博客标签实体
 *
 * <p>映射 blog_tag 表，支持逻辑删除。</p>
 *
 * @author hunhiong
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("blog_tag")
public class BlogTag extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 标签名称 */
    private String name;

    /** 标签别名（URL友好） */
    private String slug;

    /** 逻辑删除：0-未删除，1-已删除 */
    @TableLogic
    private Integer deleted;
}
