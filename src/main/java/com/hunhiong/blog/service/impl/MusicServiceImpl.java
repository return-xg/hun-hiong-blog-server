package com.hunhiong.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hunhiong.blog.common.enums.FileType;
import com.hunhiong.blog.common.enums.StatusEnum;
import com.hunhiong.blog.common.exception.BusinessException;
import com.hunhiong.blog.common.exception.ErrorCode;
import com.hunhiong.blog.common.result.PageResult;
import com.hunhiong.blog.converter.BlogMusicConverter;
import com.hunhiong.blog.dto.MusicDTO;
import com.hunhiong.blog.dto.MusicQueryDTO;
import com.hunhiong.blog.entity.BlogMusic;
import com.hunhiong.blog.mapper.BlogMusicMapper;
import com.hunhiong.blog.service.FileService;
import com.hunhiong.blog.service.MusicService;
import com.hunhiong.blog.vo.FileVO;
import com.hunhiong.blog.vo.MusicVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 音乐管理服务实现
 *
 * @author hunhiong
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MusicServiceImpl implements MusicService {

    private final BlogMusicMapper blogMusicMapper;
    private final BlogMusicConverter blogMusicConverter;
    private final FileService fileService;

    @Override
    public List<MusicVO> list() {
        // 查询启用状态的音乐，按 sort 升序、create_time 降序
        LambdaQueryWrapper<BlogMusic> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlogMusic::getStatus, StatusEnum.ENABLED.getCode())
                .orderByAsc(BlogMusic::getSort)
                .orderByDesc(BlogMusic::getCreateTime);

        List<BlogMusic> entities = blogMusicMapper.selectList(wrapper);

        return entities.stream()
                .map(blogMusicConverter::entityToVO)
                .toList();
    }

    @Override
    public MusicVO upload(MultipartFile file, String title, String artist) {
        // 调用文件服务上传音乐文件
        FileVO fileVO = fileService.upload(file, FileType.MUSIC);

        // 创建音乐记录
        BlogMusic music = new BlogMusic();
        music.setFileId(fileVO.getId());
        music.setTitle(title);
        music.setArtist(artist);
        music.setUrl(fileVO.getUrl());
        music.setStatus(StatusEnum.ENABLED.getCode());
        music.setSort(0);
        blogMusicMapper.insert(music);

        log.info("音乐上传成功: title={}, artist={}, url={}", title, artist, fileVO.getUrl());

        return blogMusicConverter.entityToVO(music);
    }

    @Override
    public PageResult<MusicVO> page(MusicQueryDTO queryDTO) {
        // 构建分页参数
        Page<BlogMusic> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());

        // 构建查询条件：按歌曲名称/歌手模糊搜索、状态过滤，按 sort 升序、create_time 降序
        LambdaQueryWrapper<BlogMusic> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(queryDTO.getTitle())) {
            wrapper.like(BlogMusic::getTitle, queryDTO.getTitle());
        }
        if (StringUtils.hasText(queryDTO.getArtist())) {
            wrapper.like(BlogMusic::getArtist, queryDTO.getArtist());
        }
        if (queryDTO.getStatus() != null) {
            wrapper.eq(BlogMusic::getStatus, queryDTO.getStatus());
        }
        wrapper.orderByAsc(BlogMusic::getSort)
                .orderByDesc(BlogMusic::getCreateTime);

        Page<BlogMusic> result = blogMusicMapper.selectPage(page, wrapper);

        // 转换为 VO 列表
        List<MusicVO> voList = result.getRecords().stream()
                .map(blogMusicConverter::entityToVO)
                .toList();

        return new PageResult<>(result.getCurrent(), result.getSize(), result.getTotal(), voList);
    }

    @Override
    public MusicVO getById(Long id) {
        BlogMusic entity = blogMusicMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.MUSIC_NOT_FOUND);
        }
        return blogMusicConverter.entityToVO(entity);
    }

    @Override
    public void update(Long id, MusicDTO dto) {
        // 查询音乐是否存在
        BlogMusic existing = blogMusicMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.MUSIC_NOT_FOUND);
        }

        // 更新非空字段
        if (StringUtils.hasText(dto.getTitle())) {
            existing.setTitle(dto.getTitle());
        }
        if (dto.getArtist() != null) {
            existing.setArtist(dto.getArtist());
        }
        if (dto.getCover() != null) {
            existing.setCover(dto.getCover());
        }
        if (dto.getDuration() != null) {
            existing.setDuration(dto.getDuration());
        }
        if (dto.getSort() != null) {
            existing.setSort(dto.getSort());
        }
        if (dto.getStatus() != null) {
            existing.setStatus(dto.getStatus());
        }

        blogMusicMapper.updateById(existing);

        log.info("修改音乐成功: id={}", id);
    }

    @Override
    public void batchDelete(List<Long> ids) {
        for (Long id : ids) {
            BlogMusic existing = blogMusicMapper.selectById(id);
            if (existing == null) {
                throw new BusinessException(ErrorCode.MUSIC_NOT_FOUND);
            }
            blogMusicMapper.deleteById(id);
        }
        log.info("批量删除音乐成功: ids={}", ids);
    }
}
