package com.hunhiong.blog.service;

import com.hunhiong.blog.common.result.PageResult;
import com.hunhiong.blog.dto.CategoryDTO;
import com.hunhiong.blog.dto.CategoryQueryDTO;
import com.hunhiong.blog.vo.CategoryVO;

import java.util.List;

/**
 * 分类管理服务接口
 *
 * <p>提供分类的增删改查、分页查询、全量列表等能力。</p>
 *
 * @author hunhiong
 */
public interface CategoryService {

    /**
     * 新增分类
     *
     * @param dto 分类请求参数
     */
    void create(CategoryDTO dto);

    /**
     * 修改分类
     *
     * @param id  分类ID
     * @param dto 分类请求参数
     */
    void update(Long id, CategoryDTO dto);

    /**
     * 批量删除分类（逻辑删除）
     *
     * @param ids 分类ID列表
     */
    void batchDelete(List<Long> ids);

    /**
     * 根据ID查询分类详情
     *
     * @param id 分类ID
     * @return 分类信息
     */
    CategoryVO getById(Long id);

    /**
     * 分页查询分类
     *
     * @param queryDTO 分页查询参数
     * @return 分页结果
     */
    PageResult<CategoryVO> page(CategoryQueryDTO queryDTO);

    /**
     * 获取全部分类列表（按 sort 升序）
     *
     * @return 分类列表
     */
    List<CategoryVO> list();
}
