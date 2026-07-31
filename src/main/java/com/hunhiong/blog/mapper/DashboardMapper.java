package com.hunhiong.blog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
     * 查询各分类下的已发布文章数量，按文章数降序排列
     *
     * @return 每行包含 category_name、article_count 字段
     */
    @Select("SELECT c.name AS category_name, COUNT(a.id) AS article_count "
            + "FROM blog_category c "
            + "LEFT JOIN blog_article a ON a.category_id = c.id AND a.status = 1 AND a.deleted = 0 "
            + "WHERE c.deleted = 0 "
            + "GROUP BY c.id, c.name "
            + "HAVING COUNT(a.id) > 0 "
            + "ORDER BY article_count DESC")
    List<Map<String, Object>> selectCategoryDistribution();

    /**
     * 查询浏览量最高的 5 篇已发布文章
     *
     * @return 每行包含 id、title、view_count 字段
     */
    @Select("SELECT CAST(id AS VARCHAR) AS id, title, view_count "
            + "FROM blog_article "
            + "WHERE status = 1 AND deleted = 0 "
            + "ORDER BY view_count DESC "
            + "LIMIT 5")
    List<Map<String, Object>> selectTopArticles();

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

    /**
     * 批量查询文章关联的标签信息
     *
     * @param articleIds 文章ID列表
     * @return 每行包含 article_id、tag_id、tag_name 字段
     */
    @Select("<script>" +
            "SELECT CAST(at.article_id AS VARCHAR) AS article_id, " +
            "CAST(t.id AS VARCHAR) AS tag_id, t.name AS tag_name " +
            "FROM blog_article_tag at " +
            "INNER JOIN blog_tag t ON at.tag_id = t.id AND t.deleted = 0 " +
            "WHERE at.article_id IN " +
            "<foreach item='id' collection='articleIds' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<Map<String, Object>> selectTagsByArticleIds(@Param("articleIds") List<String> articleIds);
}
