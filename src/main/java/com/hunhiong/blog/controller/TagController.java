package com.hunhiong.blog.controller;

import com.hunhiong.blog.common.result.PageResult;
import com.hunhiong.blog.common.result.Result;
import com.hunhiong.blog.dto.TagDTO;
import com.hunhiong.blog.dto.TagQueryDTO;
import com.hunhiong.blog.service.TagService;
import com.hunhiong.blog.vo.TagVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 标签管理控制器
 *
 * @author hunhiong
 */
@Tag(name = "标签管理")
@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    /**
     * 新增标签
     */
    @Operation(summary = "新增标签")
    @PostMapping
    public Result<Void> create(@Validated(TagDTO.Create.class) @RequestBody TagDTO dto) {
        tagService.create(dto);
        return Result.success();
    }

    /**
     * 修改标签
     */
    @Operation(summary = "修改标签")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody TagDTO dto) {
        tagService.update(id, dto);
        return Result.success();
    }

    /**
     * 批量删除标签
     */
    @Operation(summary = "批量删除标签")
    @DeleteMapping("/batch")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        tagService.batchDelete(ids);
        return Result.success();
    }

    /**
     * 查询标签详情
     */
    @Operation(summary = "查询标签详情")
    @GetMapping("/{id}")
    public Result<TagVO> getById(@PathVariable Long id) {
        return Result.success(tagService.getById(id));
    }

    /**
     * 分页查询标签
     */
    @Operation(summary = "分页查询标签")
    @GetMapping("/page")
    public Result<PageResult<TagVO>> page(TagQueryDTO queryDTO) {
        return Result.success(tagService.page(queryDTO));
    }

    /**
     * 获取全部标签列表
     */
    @Operation(summary = "获取全部标签列表")
    @GetMapping("/list")
    public Result<List<TagVO>> list() {
        return Result.success(tagService.list());
    }
}
