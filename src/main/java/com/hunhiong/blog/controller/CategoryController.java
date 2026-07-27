package com.hunhiong.blog.controller;

import com.hunhiong.blog.common.result.PageResult;
import com.hunhiong.blog.dto.CategoryDTO;
import com.hunhiong.blog.dto.CategoryQueryDTO;
import com.hunhiong.blog.service.CategoryService;
import com.hunhiong.blog.vo.CategoryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hunhiong.blog.common.result.Result;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * 分类管理控制器
 *
 * @author hunhiong
 */
@Tag(name = "分类管理")
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 新增分类
     */
    @Operation(summary = "新增分类")
    @PostMapping
    public Result<Void> create(@Validated(CategoryDTO.Create.class) @RequestBody CategoryDTO dto) {
        categoryService.create(dto);
        return Result.success();
    }

    /**
     * 批量删除分类
     */
    @Operation(summary = "批量删除分类")
    @DeleteMapping("/batch")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        categoryService.batchDelete(ids);
        return Result.success();
    }

    /**
     * 修改分类
     */
    @Operation(summary = "修改分类")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody CategoryDTO dto) {
        categoryService.update(id, dto);
        return Result.success();
    }

    /**
     * 查询分类详情
     */
    @Operation(summary = "查询分类详情")
    @GetMapping("/{id}")
    public Result<CategoryVO> getById(@PathVariable Long id) {
        return Result.success(categoryService.getById(id));
    }

    /**
     * 分页查询分类
     */
    @Operation(summary = "分页查询分类")
    @GetMapping("/page")
    public Result<PageResult<CategoryVO>> page(CategoryQueryDTO queryDTO) {
        return Result.success(categoryService.page(queryDTO));
    }

    /**
     * 获取全部分类列表
     */
    @Operation(summary = "获取全部分类列表")
    @GetMapping("/list")
    public Result<List<CategoryVO>> list() {
        return Result.success(categoryService.list());
    }
}
