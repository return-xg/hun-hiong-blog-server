package com.hunhiong.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hunhiong.blog.entity.BlogArticle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 博客文章 Mapper
 *
 * @author hunhiong
 */
@Mapper
public interface BlogArticleMapper extends BaseMapper<BlogArticle> {

    /**
     * 增加文章浏览量
     *
     * @param articleId 文章ID
     * @param delta     增量
     * @return 影响行数
     */
    @Update("UPDATE blog_article SET view_count = view_count + #{delta} WHERE id = #{articleId} AND deleted = 0")
    int incrementViewCount(@Param("articleId") Long articleId, @Param("delta") long delta);

    /**
     * 增加文章点赞量
     *
     * @param articleId 文章ID
     * @param delta     增量
     * @return 影响行数
     */
    @Update("UPDATE blog_article SET like_count = like_count + #{delta} WHERE id = #{articleId} AND deleted = 0")
    int incrementLikeCount(@Param("articleId") Long articleId, @Param("delta") long delta);
}
