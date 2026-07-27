package com.hunhiong.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hunhiong.blog.common.exception.BusinessException;
import com.hunhiong.blog.common.exception.ErrorCode;
import com.hunhiong.blog.common.result.PageResult;
import com.hunhiong.blog.converter.BlogCategoryConverter;
import com.hunhiong.blog.dto.CategoryDTO;
import com.hunhiong.blog.dto.CategoryQueryDTO;
import com.hunhiong.blog.entity.BlogCategory;
import com.hunhiong.blog.mapper.BlogCategoryMapper;
import com.hunhiong.blog.service.CategoryService;
import com.hunhiong.blog.vo.CategoryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 分类管理服务实现
 *
 * @author hunhiong
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final BlogCategoryMapper blogCategoryMapper;
    private final BlogCategoryConverter blogCategoryConverter;

    @Override
    public void create(CategoryDTO dto) {
        // 校验分类名称唯一性
        checkNameUnique(dto.getName(), null);

        // 校验分类别名唯一性（如果传了 slug）
        if (StringUtils.hasText(dto.getSlug())) {
            checkSlugUnique(dto.getSlug(), null);
        }

        // 构建实体并入库
        BlogCategory entity = blogCategoryConverter.dtoToEntity(dto);
        blogCategoryMapper.insert(entity);

        log.info("新增分类成功: id={}, name={}", entity.getId(), entity.getName());
    }

    @Override
    public void update(Long id, CategoryDTO dto) {
        // 查询分类是否存在
        BlogCategory existing = blogCategoryMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        // 校验分类名称唯一性（排除自身）
        if (StringUtils.hasText(dto.getName())) {
            checkNameUnique(dto.getName(), id);
            existing.setName(dto.getName());
        }

        // 校验分类别名唯一性（排除自身）
        if (dto.getSlug() != null) {
            checkSlugUnique(dto.getSlug(), id);
            existing.setSlug(dto.getSlug());
        }

        // 更新其他字段
        if (dto.getSort() != null) {
            existing.setSort(dto.getSort());
        }
        if (dto.getDescription() != null) {
            existing.setDescription(dto.getDescription());
        }

        blogCategoryMapper.updateById(existing);

        log.info("修改分类成功: id={}", id);
    }

    /**
     * 删除单个分类（逻辑删除），校验关联文章
     *
     * @param id 分类ID
     */
    private void deleteSingle(Long id) {
        // 查询分类是否存在
        BlogCategory existing = blogCategoryMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        // 检查是否有关联文章
        long articleCount = blogCategoryMapper.countArticlesByCategoryId(id);
        if (articleCount > 0) {
            throw new BusinessException(ErrorCode.CATEGORY_HAS_ARTICLES);
        }

        blogCategoryMapper.deleteById(id);

        log.info("删除分类成功: id={}, name={}", id, existing.getName());
    }

    @Override
    public void batchDelete(List<Long> ids) {
        for (Long id : ids) {
            deleteSingle(id);
        }
        log.info("批量删除分类成功: ids={}", ids);
    }

    @Override
    public CategoryVO getById(Long id) {
        BlogCategory entity = blogCategoryMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        return blogCategoryConverter.entityToVO(entity);
    }

    @Override
    public PageResult<CategoryVO> page(CategoryQueryDTO queryDTO) {
        // 构建分页参数
        Page<BlogCategory> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());

        // 构建查询条件：按名称模糊搜索，按 sort 升序
        LambdaQueryWrapper<BlogCategory> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(queryDTO.getName())) {
            wrapper.like(BlogCategory::getName, queryDTO.getName());
        }
        wrapper.orderByAsc(BlogCategory::getSort);

        Page<BlogCategory> result = blogCategoryMapper.selectPage(page, wrapper);

        // 转换为 VO 列表
        List<CategoryVO> voList = result.getRecords().stream()
                .map(blogCategoryConverter::entityToVO)
                .toList();

        return new PageResult<>(result.getCurrent(), result.getSize(), result.getTotal(), voList);
    }

    @Override
    public List<CategoryVO> list() {
        // 按 sort 升序、create_time 降序
        LambdaQueryWrapper<BlogCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(BlogCategory::getSort)
                .orderByDesc(BlogCategory::getCreateTime);

        List<BlogCategory> entities = blogCategoryMapper.selectList(wrapper);

        return entities.stream()
                .map(blogCategoryConverter::entityToVO)
                .toList();
    }

    /**
     * 校验分类名称唯一性（未删除范围内）
     *
     * @param name    分类名称
     * @param excludeId 排除的分类ID（修改时排除自身）
     */
    private void checkNameUnique(String name, Long excludeId) {
        LambdaQueryWrapper<BlogCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlogCategory::getName, name);
        if (excludeId != null) {
            wrapper.ne(BlogCategory::getId, excludeId);
        }
        if (blogCategoryMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ErrorCode.CATEGORY_NAME_EXISTS);
        }
    }

    /**
     * 校验分类别名唯一性（未删除范围内）
     *
     * @param slug      分类别名
     * @param excludeId 排除的分类ID（修改时排除自身）
     */
    private void checkSlugUnique(String slug, Long excludeId) {
        LambdaQueryWrapper<BlogCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlogCategory::getSlug, slug);
        if (excludeId != null) {
            wrapper.ne(BlogCategory::getId, excludeId);
        }
        if (blogCategoryMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ErrorCode.CATEGORY_SLUG_EXISTS);
        }
    }
}
