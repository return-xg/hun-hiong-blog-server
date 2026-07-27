package com.hunhiong.blog.converter;

import com.hunhiong.blog.dto.TagDTO;
import com.hunhiong.blog.entity.BlogTag;
import com.hunhiong.blog.vo.TagVO;
import org.springframework.stereotype.Component;

/**
 * 博客标签对象转换器
 *
 * <p>负责 BlogTag Entity 与 TagVO / TagDTO 之间的转换。</p>
 *
 * @author hunhiong
 */
@Component
public class BlogTagConverter {

    /**
     * BlogTag Entity 转 TagVO
     *
     * @param entity 标签实体
     * @return 标签 VO，入参为 null 时返回 null
     */
    public TagVO entityToVO(BlogTag entity) {
        if (entity == null) {
            return null;
        }
        TagVO vo = new TagVO();
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setSlug(entity.getSlug());
        vo.setCreateTime(entity.getCreateTime());
        return vo;
    }

    /**
     * TagDTO 转 BlogTag Entity
     *
     * @param dto 标签请求 DTO
     * @return 标签实体，入参为 null 时返回 null
     */
    public BlogTag dtoToEntity(TagDTO dto) {
        if (dto == null) {
            return null;
        }
        BlogTag entity = new BlogTag();
        entity.setName(dto.getName());
        entity.setSlug(dto.getSlug());
        return entity;
    }
}
