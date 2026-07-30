package com.hunhiong.blog.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章信息 VO
 *
 * <p>返回前端文章基本信息，包含分类名称与标签列表。</p>
 *
 * @author hunhiong
 */
@Data
public class ArticleVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 文章ID */
    private Long id;

    /** 文章标题 */
    private String title;

    /** 文章摘要 */
    private String summary;

    /** 文章内容（富文本 HTML），仅详情接口返回，列表接口为 null 时不序列化 */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String content;

    /** 封面图URL */
    private String coverUrl;

    /** 分类ID */
    private Long categoryId;

    /** 分类名称 */
    private String categoryName;

    /** 状态：0-草稿，1-已发布，2-下线 */
    private Integer status;

    /** 浏览量 */
    private Long viewCount;

    /** 点赞量 */
    private Long likeCount;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 标签列表 */
    private List<TagVO> tags;
}
