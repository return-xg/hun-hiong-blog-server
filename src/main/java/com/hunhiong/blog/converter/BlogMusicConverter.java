package com.hunhiong.blog.converter;

import com.hunhiong.blog.dto.MusicDTO;
import com.hunhiong.blog.entity.BlogMusic;
import com.hunhiong.blog.vo.MusicVO;
import org.springframework.stereotype.Component;

/**
 * 博客音乐对象转换器
 *
 * <p>负责 BlogMusic Entity 与 MusicVO 之间的转换。</p>
 *
 * @author hunhiong
 */
@Component
public class BlogMusicConverter {

    /**
     * BlogMusic Entity 转 MusicVO
     *
     * @param entity 音乐实体
     * @return 音乐 VO，入参为 null 时返回 null
     */
    public MusicVO entityToVO(BlogMusic entity) {
        if (entity == null) {
            return null;
        }
        MusicVO vo = new MusicVO();
        vo.setId(entity.getId());
        vo.setTitle(entity.getTitle());
        vo.setArtist(entity.getArtist());
        vo.setCover(entity.getCover());
        vo.setUrl(entity.getUrl());
        vo.setDuration(entity.getDuration());
        vo.setSort(entity.getSort());
        vo.setStatus(entity.getStatus());
        return vo;
    }

    /**
     * MusicDTO 转 BlogMusic Entity
     *
     * @param dto 音乐请求参数
     * @return 音乐实体，入参为 null 时返回 null
     */
    public BlogMusic dtoToEntity(MusicDTO dto) {
        if (dto == null) {
            return null;
        }
        BlogMusic entity = new BlogMusic();
        entity.setTitle(dto.getTitle());
        entity.setArtist(dto.getArtist());
        entity.setCover(dto.getCover());
        entity.setDuration(dto.getDuration());
        entity.setSort(dto.getSort());
        entity.setStatus(dto.getStatus());
        return entity;
    }
}
