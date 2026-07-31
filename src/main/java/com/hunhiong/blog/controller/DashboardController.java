package com.hunhiong.blog.controller;

import com.hunhiong.blog.common.result.Result;
import com.hunhiong.blog.service.DashboardService;
import com.hunhiong.blog.vo.CategoryDistributionVO;
import com.hunhiong.blog.vo.DashboardOverviewVO;
import com.hunhiong.blog.vo.DashboardRecentArticleVO;
import com.hunhiong.blog.vo.TopArticleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 仪表盘控制器
 *
 * <p>提供概览统计、分类分布、热门文章、最近文章等仪表盘数据接口，所有接口需要登录鉴权。</p>
 *
 * @author hunhiong
 */
@Tag(name = "仪表盘")
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * 获取概览统计数据
     */
    @Operation(summary = "概览统计")
    @GetMapping("/overview")
    public Result<DashboardOverviewVO> overview() {
        return Result.success(dashboardService.getOverview());
    }

    /**
     * 获取各分类下的已发布文章数量
     */
    @Operation(summary = "分类文章分布")
    @GetMapping("/category-distribution")
    public Result<List<CategoryDistributionVO>> categoryDistribution() {
        return Result.success(dashboardService.getCategoryDistribution());
    }

    /**
     * 获取浏览量最高的 5 篇文章
     */
    @Operation(summary = "热门文章 Top 5")
    @GetMapping("/top-articles")
    public Result<List<TopArticleVO>> topArticles() {
        return Result.success(dashboardService.getTopArticles());
    }

    /**
     * 获取最近文章
     */
    @Operation(summary = "最近文章")
    @GetMapping("/recent-articles")
    public Result<List<DashboardRecentArticleVO>> recentArticles() {
        return Result.success(dashboardService.getRecentArticles());
    }
}
