package com.hunhiong.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 文章请求 DTO（新增 / 修改通用）
 *
 * <p>新增时 title 为必填，修改时所有字段均为可选。</p>
 *
 * @author hunhiong
 */
@Data
public class ArticleDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 文章标题 */
    @NotBlank(groups = Create.class, message = "文章标题不能为空")
    @Size(max = 255, message = "文章标题长度不能超过255")
    private String title;

    /** 文章摘要 */
    @Size(max = 500, message = "文章摘要长度不能超过500")
    private String summary;

    /** 文章内容（Markdown/HTML） */
    private String content;

    /** 封面图URL */
    @Size(max = 255, message = "封面图URL长度不能超过255")
    private String coverUrl;

    /** 分类ID */
    private Long categoryId;

    /** 状态：0-草稿，1-已发布，2-下线 */
    private Integer status;

    /** 标签ID列表 */
    private List<Long> tagIds;

    /**
     * 新增校验分组标记
     */
    public interface Create {
    }
}
