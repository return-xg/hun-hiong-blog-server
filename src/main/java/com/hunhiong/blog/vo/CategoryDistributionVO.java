package com.hunhiong.blog.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分类文章分布 VO
 *
 * <p>返回每个分类下的已发布文章数量。</p>
 *
 * @author hunhiong
 */
@Data
public class CategoryDistributionVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 分类名称 */
    private String categoryName;

    /** 该分类下的已发布文章数 */
    private Long articleCount;
}
