package com.hunhiong.blog.service;

import com.hunhiong.blog.vo.DashboardOverviewVO;
import com.hunhiong.blog.vo.DashboardRecentArticleVO;
import com.hunhiong.blog.vo.DashboardTrendVO;

import java.util.List;

/**
 * 仪表盘服务接口
 *
 * <p>提供概览统计、浏览趋势、最近文章等仪表盘数据查询能力。</p>
 *
 * @author hunhiong
 */
public interface DashboardService {

    /**
     * 获取概览统计数据
     *
     * @return 文章数、分类数、标签数、浏览量总和、点赞数总和
     */
    DashboardOverviewVO getOverview();

    /**
     * 获取近 7 天浏览趋势
     *
     * @return 7 天的日期与浏览量列表，按日期升序，无数据的日期补 0
     */
    List<DashboardTrendVO> getTrend();

    /**
     * 获取最近发布的 8 篇文章
     *
     * @return 最近文章列表，按创建时间倒序
     */
    List<DashboardRecentArticleVO> getRecentArticles();
}
