package com.hunhiong.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hunhiong.blog.common.constants.RedisConstants;
import com.hunhiong.blog.common.enums.ArticleStatusEnum;
import com.hunhiong.blog.common.exception.BusinessException;
import com.hunhiong.blog.common.exception.ErrorCode;
import com.hunhiong.blog.common.result.PageResult;
import com.hunhiong.blog.converter.BlogArticleConverter;
import com.hunhiong.blog.converter.BlogTagConverter;
import com.hunhiong.blog.dto.ArticleDTO;
import com.hunhiong.blog.dto.ArticleQueryDTO;
import com.hunhiong.blog.entity.BlogArticle;
import com.hunhiong.blog.entity.BlogArticleTag;
import com.hunhiong.blog.entity.BlogCategory;
import com.hunhiong.blog.entity.BlogTag;
import com.hunhiong.blog.mapper.BlogArticleMapper;
import com.hunhiong.blog.mapper.BlogArticleTagMapper;
import com.hunhiong.blog.mapper.BlogCategoryMapper;
import com.hunhiong.blog.mapper.BlogTagMapper;
import com.hunhiong.blog.service.ArticleService;
import com.hunhiong.blog.utils.RedisService;
import com.hunhiong.blog.vo.ArticleVO;
import com.hunhiong.blog.vo.TagVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 文章管理服务实现
 *
 * @author hunhiong
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final BlogArticleMapper blogArticleMapper;
    private final BlogArticleTagMapper blogArticleTagMapper;
    private final BlogCategoryMapper blogCategoryMapper;
    private final BlogTagMapper blogTagMapper;
    private final BlogArticleConverter blogArticleConverter;
    private final BlogTagConverter blogTagConverter;
    private final RedisService redisService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ArticleDTO dto) {
        // 校验标题唯一性
        checkTitleUnique(dto.getTitle(), null);

        // 校验分类是否存在
        if (dto.getCategoryId() != null) {
            checkCategoryExists(dto.getCategoryId());
        }

        // 构建实体并入库
        BlogArticle entity = blogArticleConverter.dtoToEntity(dto);
        // 设置默认值
        if (entity.getStatus() == null) {
            entity.setStatus(ArticleStatusEnum.DRAFT.getCode());
        }
        if (entity.getViewCount() == null) {
            entity.setViewCount(0L);
        }
        if (entity.getLikeCount() == null) {
            entity.setLikeCount(0L);
        }
        blogArticleMapper.insert(entity);

        // 写入文章标签关联
        saveArticleTags(entity.getId(), dto.getTagIds());

        log.info("新增文章成功: id={}, title={}", entity.getId(), entity.getTitle());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, ArticleDTO dto) {
        // 查询文章是否存在
        BlogArticle existing = blogArticleMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        // 校验标题唯一性（排除自身）
        if (StringUtils.hasText(dto.getTitle())) {
            checkTitleUnique(dto.getTitle(), id);
            existing.setTitle(dto.getTitle());
        }

        // 校验分类是否存在并更新
        if (dto.getCategoryId() != null) {
            checkCategoryExists(dto.getCategoryId());
            existing.setCategoryId(dto.getCategoryId());
        }

        // 更新其他非空字段
        if (dto.getSummary() != null) {
            existing.setSummary(dto.getSummary());
        }
        if (dto.getContent() != null) {
            existing.setContent(dto.getContent());
        }
        if (dto.getCoverUrl() != null) {
            existing.setCoverUrl(dto.getCoverUrl());
        }
        if (dto.getStatus() != null) {
            // 校验状态值合法性
            ArticleStatusEnum.of(dto.getStatus());
            existing.setStatus(dto.getStatus());
        }

        blogArticleMapper.updateById(existing);

        // 更新标签关联：先删后增
        if (dto.getTagIds() != null) {
            blogArticleTagMapper.deleteByArticleId(id);
            saveArticleTags(id, dto.getTagIds());
        }

        log.info("修改文章成功: id={}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        // 清理文章标签关联
        blogArticleTagMapper.deleteByArticleIds(ids);
        // 逻辑删除文章
        blogArticleMapper.deleteBatchIds(ids);
        // 清理 Redis 增量缓存
        for (Long id : ids) {
            redisService.delete(RedisConstants.ARTICLE_VIEW_PREFIX + id);
            redisService.delete(RedisConstants.ARTICLE_LIKE_PREFIX + id);
        }
        log.info("批量删除文章成功: ids={}", ids);
    }

    @Override
    public ArticleVO getById(Long id) {
        BlogArticle entity = blogArticleMapper.selectById(id);
        if (entity == null) {
            throw new BusinessException(ErrorCode.ARTICLE_NOT_FOUND);
        }
        ArticleVO vo = blogArticleConverter.entityToVO(entity);
        // 填充分类名称
        fillCategoryName(vo);
        // 填充标签列表
        fillTags(vo);
        return vo;
    }

    @Override
    public PageResult<ArticleVO> page(ArticleQueryDTO queryDTO) {
        // 构建分页参数
        Page<BlogArticle> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());

        // 构建查询条件，列表查询不查 content 列，避免返回大量富文本数据
        LambdaQueryWrapper<BlogArticle> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(BlogArticle.class, field -> !"content".equals(field.getColumn()));
        if (StringUtils.hasText(queryDTO.getTitle())) {
            wrapper.like(BlogArticle::getTitle, queryDTO.getTitle());
        }
        if (queryDTO.getCategoryId() != null) {
            wrapper.eq(BlogArticle::getCategoryId, queryDTO.getCategoryId());
        }
        if (queryDTO.getStatus() != null) {
            wrapper.eq(BlogArticle::getStatus, queryDTO.getStatus());
        }
        wrapper.orderByDesc(BlogArticle::getCreateTime);

        Page<BlogArticle> result = blogArticleMapper.selectPage(page, wrapper);

        // 转换为 VO 列表
        List<ArticleVO> voList = result.getRecords().stream()
                .map(blogArticleConverter::entityToVO)
                .toList();

        // 批量填充分类名称和标签列表
        if (!voList.isEmpty()) {
            batchFillCategoryNames(voList);
            batchFillTags(voList);
        }

        return new PageResult<>(result.getCurrent(), result.getSize(), result.getTotal(), voList);
    }

    @Override
    public void publish(Long id) {
        BlogArticle existing = checkArticleExists(id);
        // 校验状态：仅草稿或下线可发布
        int currentStatus = existing.getStatus();
        if (currentStatus != ArticleStatusEnum.DRAFT.getCode()
                && currentStatus != ArticleStatusEnum.OFFLINE.getCode()) {
            throw new BusinessException(ErrorCode.ARTICLE_STATUS_ERROR);
        }
        existing.setStatus(ArticleStatusEnum.PUBLISHED.getCode());
        blogArticleMapper.updateById(existing);
        log.info("发布文章成功: id={}", id);
    }

    @Override
    public void offline(Long id) {
        BlogArticle existing = checkArticleExists(id);
        // 校验状态：仅已发布可下线
        if (existing.getStatus() != ArticleStatusEnum.PUBLISHED.getCode()) {
            throw new BusinessException(ErrorCode.ARTICLE_STATUS_ERROR);
        }
        existing.setStatus(ArticleStatusEnum.OFFLINE.getCode());
        blogArticleMapper.updateById(existing);
        log.info("下线文章成功: id={}", id);
    }

    @Override
    public void incrementViewCount(Long id) {
        // 校验文章是否存在
        checkArticleExists(id);
        // 写入 Redis Hash 增量累计
        redisService.incrementHash(RedisConstants.ARTICLE_VIEW_DELTA_HASH, String.valueOf(id), 1);
        log.debug("文章浏览量 +1: id={}", id);
    }

    @Override
    public void incrementLikeCount(Long id) {
        // 校验文章是否存在
        checkArticleExists(id);
        // 写入 Redis Hash 增量累计
        redisService.incrementHash(RedisConstants.ARTICLE_LIKE_DELTA_HASH, String.valueOf(id), 1);
        log.debug("文章点赞量 +1: id={}", id);
    }

    /**
     * 校验文章标题唯一性（未删除范围内）
     *
     * @param title     文章标题
     * @param excludeId 排除的文章ID（修改时排除自身）
     */
    private void checkTitleUnique(String title, Long excludeId) {
        LambdaQueryWrapper<BlogArticle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlogArticle::getTitle, title);
        if (excludeId != null) {
            wrapper.ne(BlogArticle::getId, excludeId);
        }
        if (blogArticleMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ErrorCode.ARTICLE_TITLE_EXISTS);
        }
    }

    /**
     * 校验分类是否存在
     *
     * @param categoryId 分类ID
     */
    private void checkCategoryExists(Long categoryId) {
        BlogCategory category = blogCategoryMapper.selectById(categoryId);
        if (category == null) {
            throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);
        }
    }

    /**
     * 校验文章是否存在
     *
     * @param id 文章ID
     * @return 文章实体
     */
    private BlogArticle checkArticleExists(Long id) {
        BlogArticle article = blogArticleMapper.selectById(id);
        if (article == null) {
            throw new BusinessException(ErrorCode.ARTICLE_NOT_FOUND);
        }
        return article;
    }

    /**
     * 批量保存文章标签关联
     *
     * @param articleId 文章ID
     * @param tagIds    标签ID列表
     */
    private void saveArticleTags(Long articleId, List<Long> tagIds) {
        if (CollectionUtils.isEmpty(tagIds)) {
            return;
        }
        List<BlogArticleTag> relations = tagIds.stream()
                .map(tagId -> {
                    BlogArticleTag relation = new BlogArticleTag();
                    relation.setArticleId(articleId);
                    relation.setTagId(tagId);
                    return relation;
                })
                .toList();
        for (BlogArticleTag relation : relations) {
            blogArticleTagMapper.insert(relation);
        }
    }

    /**
     * 填充文章 VO 的分类名称
     *
     * @param vo 文章 VO
     */
    private void fillCategoryName(ArticleVO vo) {
        if (vo.getCategoryId() == null) {
            return;
        }
        BlogCategory category = blogCategoryMapper.selectById(vo.getCategoryId());
        if (category != null) {
            vo.setCategoryName(category.getName());
        }
    }

    /**
     * 填充文章 VO 的标签列表
     *
     * @param vo 文章 VO
     */
    private void fillTags(ArticleVO vo) {
        List<Long> tagIds = blogArticleTagMapper.selectTagIdsByArticleId(vo.getId());
        if (CollectionUtils.isEmpty(tagIds)) {
            vo.setTags(Collections.emptyList());
            return;
        }
        List<BlogTag> tags = blogTagMapper.selectBatchIds(tagIds);
        List<TagVO> tagVOList = tags.stream()
                .map(blogTagConverter::entityToVO)
                .toList();
        vo.setTags(tagVOList);
    }

    /**
     * 批量填充文章 VO 列表的分类名称
     *
     * @param voList 文章 VO 列表
     */
    private void batchFillCategoryNames(List<ArticleVO> voList) {
        // 收集所有不重复的分类ID
        Set<Long> categoryIds = voList.stream()
                .map(ArticleVO::getCategoryId)
                .filter(id -> id != null)
                .collect(Collectors.toSet());
        if (categoryIds.isEmpty()) {
            return;
        }
        // 批量查询分类，构建 id → name 映射
        List<BlogCategory> categories = blogCategoryMapper.selectBatchIds(categoryIds);
        Map<Long, String> categoryNameMap = categories.stream()
                .collect(Collectors.toMap(BlogCategory::getId, BlogCategory::getName));
        // 填充分类名称
        voList.forEach(vo -> {
            if (vo.getCategoryId() != null) {
                vo.setCategoryName(categoryNameMap.get(vo.getCategoryId()));
            }
        });
    }

    /**
     * 批量填充文章 VO 列表的标签列表
     *
     * @param voList 文章 VO 列表
     */
    private void batchFillTags(List<ArticleVO> voList) {
        // 收集所有文章ID
        List<Long> articleIds = voList.stream()
                .map(ArticleVO::getId)
                .toList();

        // 查询每篇文章的标签ID，并收集所有不重复的标签ID
        List<Long> allTagIds = new ArrayList<>();
        for (ArticleVO vo : voList) {
            List<Long> tagIds = blogArticleTagMapper.selectTagIdsByArticleId(vo.getId());
            allTagIds.addAll(tagIds);
        }
        // 去重后批量查询标签
        Set<Long> uniqueTagIds = allTagIds.stream().distinct().collect(Collectors.toSet());
        if (uniqueTagIds.isEmpty()) {
            voList.forEach(vo -> vo.setTags(Collections.emptyList()));
            return;
        }
        List<BlogTag> allTags = blogTagMapper.selectBatchIds(uniqueTagIds);
        Map<Long, TagVO> tagVOMap = allTags.stream()
                .collect(Collectors.toMap(BlogTag::getId, blogTagConverter::entityToVO));

        // 为每篇文章组装标签列表
        for (ArticleVO vo : voList) {
            List<Long> tagIds = blogArticleTagMapper.selectTagIdsByArticleId(vo.getId());
            if (tagIds.isEmpty()) {
                vo.setTags(Collections.emptyList());
            } else {
                List<TagVO> tags = tagIds.stream()
                        .map(tagVOMap::get)
                        .filter(t -> t != null)
                        .toList();
                vo.setTags(tags);
            }
        }
    }
}
