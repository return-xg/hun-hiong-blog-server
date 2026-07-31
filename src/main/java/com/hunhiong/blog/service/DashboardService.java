package com.hunhiong.blog.service;

import com.hunhiong.blog.vo.CategoryDistributionVO;
import com.hunhiong.blog.vo.DashboardOverviewVO;
import com.hunhiong.blog.vo.DashboardRecentArticleVO;
import com.hunhiong.blog.vo.TopArticleVO;

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
     * 获取各分类下的已发布文章数量
     *
     * @return 分类文章分布列表，按文章数降序
     */
    List<CategoryDistributionVO> getCategoryDistribution();

    /**
     * 获取浏览量最高的 5 篇已发布文章
     *
     * @return 热门文章列表，按浏览量降序
     */
    List<TopArticleVO> getTopArticles();

    /**
     * 获取最近发布的 8 篇文章
     *
     * @return 最近文章列表，按创建时间倒序
     */
    List<DashboardRecentArticleVO> getRecentArticles();
}
