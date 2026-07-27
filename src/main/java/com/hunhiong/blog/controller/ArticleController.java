package com.hunhiong.blog.controller;

import com.hunhiong.blog.common.result.PageResult;
import com.hunhiong.blog.common.result.Result;
import com.hunhiong.blog.dto.ArticleDTO;
import com.hunhiong.blog.dto.ArticleQueryDTO;
import com.hunhiong.blog.service.ArticleService;
import com.hunhiong.blog.vo.ArticleVO;
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
 * 文章管理控制器
 *
 * @author hunhiong
 */
@Tag(name = "文章管理")
@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    /**
     * 新增文章
     */
    @Operation(summary = "新增文章")
    @PostMapping
    public Result<Void> create(@Validated(ArticleDTO.Create.class) @RequestBody ArticleDTO dto) {
        articleService.create(dto);
        return Result.success();
    }

    /**
     * 修改文章
     */
    @Operation(summary = "修改文章")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody ArticleDTO dto) {
        articleService.update(id, dto);
        return Result.success();
    }

    /**
     * 批量删除文章
     */
    @Operation(summary = "批量删除文章")
    @DeleteMapping("/batch")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        articleService.batchDelete(ids);
        return Result.success();
    }

    /**
     * 查询文章详情
     */
    @Operation(summary = "查询文章详情")
    @GetMapping("/{id}")
    public Result<ArticleVO> getById(@PathVariable Long id) {
        return Result.success(articleService.getById(id));
    }

    /**
     * 分页查询文章
     */
    @Operation(summary = "分页查询文章")
    @GetMapping("/page")
    public Result<PageResult<ArticleVO>> page(ArticleQueryDTO queryDTO) {
        return Result.success(articleService.page(queryDTO));
    }

    /**
     * 发布文章
     */
    @Operation(summary = "发布文章")
    @PutMapping("/{id}/publish")
    public Result<Void> publish(@PathVariable Long id) {
        articleService.publish(id);
        return Result.success();
    }

    /**
     * 下线文章
     */
    @Operation(summary = "下线文章")
    @PutMapping("/{id}/offline")
    public Result<Void> offline(@PathVariable Long id) {
        articleService.offline(id);
        return Result.success();
    }

    /**
     * 增加文章浏览量
     */
    @Operation(summary = "增加文章浏览量")
    @PostMapping("/{id}/view")
    public Result<Void> incrementView(@PathVariable Long id) {
        articleService.incrementViewCount(id);
        return Result.success();
    }

    /**
     * 增加文章点赞量
     */
    @Operation(summary = "增加文章点赞量")
    @PostMapping("/{id}/like")
    public Result<Void> incrementLike(@PathVariable Long id) {
        articleService.incrementLikeCount(id);
        return Result.success();
    }
}
