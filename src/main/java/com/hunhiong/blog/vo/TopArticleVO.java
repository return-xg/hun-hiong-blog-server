package com.hunhiong.blog.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 热门文章 VO
 *
 * <p>返回浏览量最高的文章摘要信息。</p>
 *
 * @author hunhiong
 */
@Data
public class TopArticleVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 文章ID */
    private String id;

    /** 文章标题 */
    private String title;

    /** 浏览量 */
    private Long viewCount;
}
