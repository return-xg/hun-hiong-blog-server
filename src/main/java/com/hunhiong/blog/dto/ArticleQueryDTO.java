package com.hunhiong.blog.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文章分页查询请求 DTO
 *
 * @author hunhiong
 */
@Data
public class ArticleQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 文章标题（模糊搜索） */
    private String title;

    /** 分类ID */
    private Long categoryId;

    /** 标签ID（按标签筛选） */
    private Long tagId;

    /** 状态：0-草稿，1-已发布，2-下线 */
    private Integer status;

    /** 当前页，默认1 */
    private Integer current = 1;

    /** 每页大小，默认10 */
    private Integer size = 10;
}
