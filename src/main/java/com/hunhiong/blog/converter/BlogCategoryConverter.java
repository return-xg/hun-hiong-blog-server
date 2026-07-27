package com.hunhiong.blog.converter;

import com.hunhiong.blog.dto.CategoryDTO;
import com.hunhiong.blog.entity.BlogCategory;
import com.hunhiong.blog.vo.CategoryVO;
import org.springframework.stereotype.Component;

/**
 * 博客分类对象转换器
 *
 * <p>负责 BlogCategory Entity 与 CategoryVO / CategoryDTO 之间的转换。</p>
 *
 * @author hunhiong
 */
@Component
public class BlogCategoryConverter {

    /**
     * BlogCategory Entity 转 CategoryVO
     *
     * @param entity 分类实体
     * @return 分类 VO，入参为 null 时返回 null
     */
    public CategoryVO entityToVO(BlogCategory entity) {
        if (entity == null) {
            return null;
        }
        CategoryVO vo = new CategoryVO();
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setSlug(entity.getSlug());
        vo.setSort(entity.getSort());
        vo.setDescription(entity.getDescription());
        vo.setCreateTime(entity.getCreateTime());
        return vo;
    }

    /**
     * CategoryDTO 转 BlogCategory Entity
     *
     * @param dto 分类请求 DTO
     * @return 分类实体，入参为 null 时返回 null
     */
    public BlogCategory dtoToEntity(CategoryDTO dto) {
        if (dto == null) {
            return null;
        }
        BlogCategory entity = new BlogCategory();
        entity.setName(dto.getName());
        entity.setSlug(dto.getSlug());
        entity.setSort(dto.getSort());
        entity.setDescription(dto.getDescription());
        return entity;
    }
}
