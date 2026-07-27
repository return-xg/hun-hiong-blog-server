package com.hunhiong.blog.converter;

import com.hunhiong.blog.dto.ArticleDTO;
import com.hunhiong.blog.entity.BlogArticle;
import com.hunhiong.blog.vo.ArticleVO;
import org.springframework.stereotype.Component;

/**
 * 博客文章对象转换器
 *
 * <p>负责 BlogArticle Entity 与 ArticleVO / ArticleDTO 之间的转换。</p>
 *
 * @author hunhiong
 */
@Component
public class BlogArticleConverter {

    /**
     * BlogArticle Entity 转 ArticleVO（不含 categoryName 和 tags，由 Service 层补充）
     *
     * @param entity 文章实体
     * @return 文章 VO，入参为 null 时返回 null
     */
    public ArticleVO entityToVO(BlogArticle entity) {
        if (entity == null) {
            return null;
        }
        ArticleVO vo = new ArticleVO();
        vo.setId(entity.getId());
        vo.setTitle(entity.getTitle());
        vo.setSummary(entity.getSummary());
        vo.setCoverUrl(entity.getCoverUrl());
        vo.setCategoryId(entity.getCategoryId());
        vo.setStatus(entity.getStatus());
        vo.setViewCount(entity.getViewCount());
        vo.setLikeCount(entity.getLikeCount());
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(entity.getUpdateTime());
        return vo;
    }

    /**
     * ArticleDTO 转 BlogArticle Entity
     *
     * @param dto 文章请求 DTO
     * @return 文章实体，入参为 null 时返回 null
     */
    public BlogArticle dtoToEntity(ArticleDTO dto) {
        if (dto == null) {
            return null;
        }
        BlogArticle entity = new BlogArticle();
        entity.setTitle(dto.getTitle());
        entity.setSummary(dto.getSummary());
        entity.setContent(dto.getContent());
        entity.setCoverUrl(dto.getCoverUrl());
        entity.setCategoryId(dto.getCategoryId());
        entity.setStatus(dto.getStatus());
        return entity;
    }
}
