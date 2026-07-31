package com.hunhiong.blog.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 仪表盘浏览趋势 VO
 *
 * <p>返回某一天的日期及当天浏览量合计。</p>
 *
 * @author hunhiong
 */
@Data
public class DashboardTrendVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 日期，格式 MM-dd */
    private String date;

    /** 当天所有文章的浏览量之和 */
    private Long viewCount;
}
