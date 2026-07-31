package com.hunhiong.blog.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 仪表盘最近文章 VO
 *
 * <p>返回最近发布的文章摘要信息，用于仪表盘展示。</p>
 *
 * @author hunhiong
 */
@Data
public class DashboardRecentArticleVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 文章ID */
    private String id;

    /** 文章标题 */
    private String title;

    /** 所属分类名称（未分类返回 null） */
    private String categoryName;

    /** 浏览量 */
    private Long viewCount;

    /** 创建时间，格式 yyyy-MM-dd HH:mm:ss */
    private LocalDateTime createTime;

    /** 关联标签列表 */
    private List<TagSimpleVO> tags;
}
