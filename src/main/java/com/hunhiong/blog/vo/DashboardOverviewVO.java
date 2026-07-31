package com.hunhiong.blog.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 仪表盘概览统计 VO
 *
 * <p>返回文章、分类、标签总数及浏览量、点赞量汇总。</p>
 *
 * @author hunhiong
 */
@Data
public class DashboardOverviewVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 文章总数（所有状态） */
    private Long articleCount;

    /** 分类总数 */
    private Long categoryCount;

    /** 标签总数 */
    private Long tagCount;

    /** 所有文章的浏览量总和 */
    private Long viewCount;

    /** 所有文章的点赞数总和 */
    private Long likeCount;
}
