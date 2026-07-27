package com.hunhiong.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hunhiong.blog.common.exception.BusinessException;
import com.hunhiong.blog.common.exception.ErrorCode;
import com.hunhiong.blog.common.result.PageResult;
import com.hunhiong.blog.converter.BlogTagConverter;
import com.hunhiong.blog.dto.TagDTO;
import com.hunhiong.blog.dto.TagQueryDTO;
import com.hunhiong.blog.entity.BlogTag;
import com.hunhiong.blog.mapper.BlogTagMapper;
import com.hunhiong.blog.service.TagService;
import com.hunhiong.blog.vo.TagVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签管理服务实现
 *
 * @author hunhiong
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final BlogTagMapper blogTagMapper;
    private final BlogTagConverter blogTagConverter;

    @Override
    public void create(TagDTO dto) {
        // 校验标签名称唯一性
        checkNameUnique(dto.getName(), null);

        // 校验标签别名唯一性（如果传了 slug）
        if (StringUtils.hasText(dto.getSlug())) {
            checkSlugUnique(dto.getSlug(), null);
        }

        // 构建实体并入库
        BlogTag entity = blogTagConverter.dtoToEntity(dto);
        blogTagMapper.insert(entity);

        log.info("新增标签成功: id={}, name={}", entity.getId(), entity.getName());
    }

    @Override
    public void update(Long id, TagDTO dto) {
        // 查询标签是否存在
        BlogTag existing = blogTagMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.TAG_NOT_FOUND);
        }

        // 校验标签名称唯一性（排除自身）
        if (StringUtils.hasText(dto.getName())) {
            checkNameUnique(dto.getName(), id);
            existing.setName(dto.getName());
        }

        // 校验标签别名唯一性（排除自身）
        if (dto.getSlug() != null) {
            checkSlugUnique(dto.getSlug(), id);
            existing.setSlug(dto.getSlug());
        }

        blogTagMapper.updateById(existing);

        log.info("修改标签成功: id={}", id);
    }

    /**
     * 删除单个标签（逻辑删除），校验关联文章
     *
     * @param id 标签ID
     */
    private void deleteSingle(Long id) {
        // 查询标签是否存在
        BlogTag existing = blogTagMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.TAG_NOT_FOUND);
        }

        // 检查是否有关联文章
        long articleCount = blogTagMapper.countArticlesByTagId(id);
        if (articleCount > 0) {
            throw new BusinessException(ErrorCode.TAG_HAS_ARTICLES);
        }

        blogTagMapper.deleteById(id);

        log.info("删除标签成功: id={}, name={}", id, existing.getName());
    }

    @Override
    public void batchDelete(List<Long> ids) {
        for (Long id : ids) {
            deleteSingle(id);
        }
        log.info("批量删除标签成功: ids={}", ids);
    }

    @Override
    public TagVO getById(Long id) {
        BlogTag entity = blogTagMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.TAG_NOT_FOUND);
        }
        return blogTagConverter.entityToVO(entity);
    }

    @Override
    public PageResult<TagVO> page(TagQueryDTO queryDTO) {
        // 构建分页参数
        Page<BlogTag> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());

        // 构建查询条件：按名称模糊搜索，按 create_time 降序
        LambdaQueryWrapper<BlogTag> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(queryDTO.getName())) {
            wrapper.like(BlogTag::getName, queryDTO.getName());
        }
        wrapper.orderByDesc(BlogTag::getCreateTime);

        Page<BlogTag> result = blogTagMapper.selectPage(page, wrapper);

        // 转换为 VO 列表
        List<TagVO> voList = result.getRecords().stream()
                .map(blogTagConverter::entityToVO)
                .toList();

        return new PageResult<>(result.getCurrent(), result.getSize(), result.getTotal(), voList);
    }

    @Override
    public List<TagVO> list() {
        // 按 create_time 降序
        LambdaQueryWrapper<BlogTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(BlogTag::getCreateTime);

        List<BlogTag> entities = blogTagMapper.selectList(wrapper);

        return entities.stream()
                .map(blogTagConverter::entityToVO)
                .toList();
    }

    /**
     * 校验标签名称唯一性（未删除范围内）
     *
     * @param name      标签名称
     * @param excludeId 排除的标签ID（修改时排除自身）
     */
    private void checkNameUnique(String name, Long excludeId) {
        LambdaQueryWrapper<BlogTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlogTag::getName, name);
        if (excludeId != null) {
            wrapper.ne(BlogTag::getId, excludeId);
        }
        if (blogTagMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ErrorCode.TAG_NAME_EXISTS);
        }
    }

    /**
     * 校验标签别名唯一性（未删除范围内）
     *
     * @param slug      标签别名
     * @param excludeId 排除的标签ID（修改时排除自身）
     */
    private void checkSlugUnique(String slug, Long excludeId) {
        LambdaQueryWrapper<BlogTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlogTag::getSlug, slug);
        if (excludeId != null) {
            wrapper.ne(BlogTag::getId, excludeId);
        }
        if (blogTagMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ErrorCode.TAG_SLUG_EXISTS);
        }
    }
}
