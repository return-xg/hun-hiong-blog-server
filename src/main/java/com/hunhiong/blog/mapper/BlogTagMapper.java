package com.hunhiong.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hunhiong.blog.entity.BlogTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 博客标签 Mapper
 *
 * @author hunhiong
 */
@Mapper
public interface BlogTagMapper extends BaseMapper<BlogTag> {

    /**
     * 统计标签下关联的文章数量
     *
     * @param tagId 标签ID
     * @return 文章数量
     */
    @Select("SELECT COUNT(*) FROM blog_article_tag WHERE tag_id = #{tagId}")
    long countArticlesByTagId(@Param("tagId") Long tagId);
}
