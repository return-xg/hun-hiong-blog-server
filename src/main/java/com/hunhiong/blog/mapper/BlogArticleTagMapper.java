package com.hunhiong.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hunhiong.blog.entity.BlogArticleTag;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 博客文章标签关联 Mapper
 *
 * @author hunhiong
 */
@Mapper
public interface BlogArticleTagMapper extends BaseMapper<BlogArticleTag> {

    /**
     * 根据文章ID删除所有标签关联
     *
     * @param articleId 文章ID
     */
    @Delete("DELETE FROM blog_article_tag WHERE article_id = #{articleId}")
    void deleteByArticleId(@Param("articleId") Long articleId);

    /**
     * 根据文章ID列表批量删除所有标签关联
     *
     * @param articleIds 文章ID列表
     */
    @Delete("<script>" +
            "DELETE FROM blog_article_tag WHERE article_id IN " +
            "<foreach item='id' collection='articleIds' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    void deleteByArticleIds(@Param("articleIds") List<Long> articleIds);

    /**
     * 根据文章ID查询关联的标签ID列表
     *
     * @param articleId 文章ID
     * @return 标签ID列表
     */
    @Select("SELECT tag_id FROM blog_article_tag WHERE article_id = #{articleId}")
    List<Long> selectTagIdsByArticleId(@Param("articleId") Long articleId);
}
