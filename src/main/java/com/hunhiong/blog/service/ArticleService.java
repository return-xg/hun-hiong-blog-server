package com.hunhiong.blog.service;

import com.hunhiong.blog.common.result.PageResult;
import com.hunhiong.blog.dto.ArticleDTO;
import com.hunhiong.blog.dto.ArticleQueryDTO;
import com.hunhiong.blog.vo.ArticleVO;

import java.util.List;

/**
 * 文章管理服务接口
 *
 * <p>提供文章的增删改查、分页查询、发布与下线等能力。</p>
 *
 * @author hunhiong
 */
public interface ArticleService {

    /**
     * 新增文章
     *
     * @param dto 文章请求参数
     */
    void create(ArticleDTO dto);

    /**
     * 修改文章
     *
     * @param id  文章ID
     * @param dto 文章请求参数
     */
    void update(Long id, ArticleDTO dto);

    /**
     * 批量删除文章（逻辑删除，同时清理标签关联）
     *
     * @param ids 文章ID列表
     */
    void batchDelete(List<Long> ids);

    /**
     * 根据ID查询文章详情
     *
     * @param id 文章ID
     * @return 文章信息（含分类名称与标签列表）
     */
    ArticleVO getById(Long id);

    /**
     * 分页查询文章
     *
     * @param queryDTO 分页查询参数
     * @return 分页结果
     */
    PageResult<ArticleVO> page(ArticleQueryDTO queryDTO);

    /**
     * 发布文章（草稿/下线 → 已发布）
     *
     * @param id 文章ID
     */
    void publish(Long id);

    /**
     * 下线文章（已发布 → 下线）
     *
     * @param id 文章ID
     */
    void offline(Long id);

    /**
     * 增加文章浏览量（写入 Redis 计数器，定时同步到数据库）
     *
     * @param id 文章ID
     */
    void incrementViewCount(Long id);

    /**
     * 增加文章点赞量（写入 Redis 计数器，定时同步到数据库）
     *
     * @param id 文章ID
     */
    void incrementLikeCount(Long id);
}
