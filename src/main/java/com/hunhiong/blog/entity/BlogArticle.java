package com.hunhiong.blog.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 博客文章实体
 *
 * <p>映射 blog_article 表，支持逻辑删除。</p>
 *
 * @author hunhiong
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("blog_article")
public class BlogArticle extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 文章标题 */
    private String title;

    /** 文章摘要 */
    private String summary;

    /** 文章内容（Markdown/HTML） */
    private String content;

    /** 封面图URL */
    private String coverUrl;

    /** 分类ID */
    private Long categoryId;

    /** 状态：0-草稿，1-已发布，2-下线 */
    private Integer status;

    /** 浏览量 */
    private Long viewCount;

    /** 点赞量 */
    private Long likeCount;

    /** 逻辑删除：0-未删除，1-已删除 */
    @TableLogic
    private Integer deleted;
}
