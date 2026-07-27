package com.hunhiong.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hunhiong.blog.entity.BlogCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 博客分类 Mapper
 *
 * @author hunhiong
 */
@Mapper
public interface BlogCategoryMapper extends BaseMapper<BlogCategory> {

    /**
     * 统计分类下关联的文章数量
     *
     * @param categoryId 分类ID
     * @return 文章数量
     */
    @Select("SELECT COUNT(*) FROM blog_article WHERE category_id = #{categoryId} AND deleted = 0")
    long countArticlesByCategoryId(@Param("categoryId") Long categoryId);
}
