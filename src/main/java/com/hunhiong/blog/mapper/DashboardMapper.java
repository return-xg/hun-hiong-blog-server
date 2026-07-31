package com.hunhiong.blog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 仪表盘数据访问层
 *
 * <p>提供仪表盘所需的聚合统计查询。</p>
 *
 * @author hunhiong
 */
@Mapper
public interface DashboardMapper {

    /**
     * 统计文章总数（所有状态，未删除）
     *
     * @return 文章数量
     */
    @Select("SELECT COUNT(*) FROM blog_article WHERE deleted = 0")
    long countArticles();

    /**
     * 统计分类总数（未删除）
     *
     * @return 分类数量
     */
    @Select("SELECT COUNT(*) FROM blog_category WHERE deleted = 0")
    long countCategories();

    /**
     * 统计标签总数（未删除）
     *
     * @return 标签数量
     */
    @Select("SELECT COUNT(*) FROM blog_tag WHERE deleted = 0")
    long countTags();

    /**
     * 统计所有文章的浏览量总和
     *
     * @return 浏览量总和，无数据时返回 0
     */
    @Select("SELECT COALESCE(SUM(view_count), 0) FROM blog_article WHERE deleted = 0")
    long sumViewCount();

    /**
     * 统计所有文章的点赞数总和
     *
     * @return 点赞数总和，无数据时返回 0
     */
    @Select("SELECT COALESCE(SUM(like_count), 0) FROM blog_article WHERE deleted = 0")
    long sumLikeCount();

    /**
     * 查询最近 7 天每天的浏览量合计
     *
     * @param startTime 起始时间（7 天前的零点）
     * @return 每行包含 date（MM-dd 格式）和 view_count 字段
     */
    @Select("SELECT TO_CHAR(DATE_TRUNC('day', create_time AT TIME ZONE 'Asia/Shanghai'), 'MM-DD') AS date, "
            + "COALESCE(SUM(view_count), 0) AS view_count "
            + "FROM blog_article "
            + "WHERE create_time >= #{startTime} AND deleted = 0 "
            + "GROUP BY DATE_TRUNC('day', create_time AT TIME ZONE 'Asia/Shanghai') "
            + "ORDER BY DATE_TRUNC('day', create_time AT TIME ZONE 'Asia/Shanghai')")
    List<Map<String, Object>> selectDailyViewCount(@Param("startTime") LocalDateTime startTime);

    /**
     * 查询最近发布的 8 篇文章（已发布状态），左连接分类表取分类名
     *
     * @return 每行包含 id、title、category_name、view_count、create_time 字段
     */
    @Select("SELECT CAST(a.id AS VARCHAR) AS id, a.title, c.name AS category_name, "
            + "a.view_count, a.create_time "
            + "FROM blog_article a "
            + "LEFT JOIN blog_category c ON a.category_id = c.id AND c.deleted = 0 "
            + "WHERE a.status = 1 AND a.deleted = 0 "
            + "ORDER BY a.create_time DESC "
            + "LIMIT 8")
    List<Map<String, Object>> selectRecentArticles();
}
