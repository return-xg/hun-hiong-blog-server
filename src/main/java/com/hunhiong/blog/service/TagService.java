package com.hunhiong.blog.service;

import com.hunhiong.blog.common.result.PageResult;
import com.hunhiong.blog.dto.TagDTO;
import com.hunhiong.blog.dto.TagQueryDTO;
import com.hunhiong.blog.vo.TagVO;

import java.util.List;

/**
 * 标签管理服务接口
 *
 * <p>提供标签的增删改查、分页查询、全量列表等能力。</p>
 *
 * @author hunhiong
 */
public interface TagService {

    /**
     * 新增标签
     *
     * @param dto 标签请求参数
     */
    void create(TagDTO dto);

    /**
     * 修改标签
     *
     * @param id  标签ID
     * @param dto 标签请求参数
     */
    void update(Long id, TagDTO dto);

    /**
     * 批量删除标签（逻辑删除）
     *
     * @param ids 标签ID列表
     */
    void batchDelete(List<Long> ids);

    /**
     * 根据ID查询标签详情
     *
     * @param id 标签ID
     * @return 标签信息
     */
    TagVO getById(Long id);

    /**
     * 分页查询标签
     *
     * @param queryDTO 分页查询参数
     * @return 分页结果
     */
    PageResult<TagVO> page(TagQueryDTO queryDTO);

    /**
     * 获取全部标签列表（按 create_time 降序）
     *
     * @return 标签列表
     */
    List<TagVO> list();
}
