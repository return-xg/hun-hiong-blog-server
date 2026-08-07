package com.hunhiong.blog.controller;

import com.hunhiong.blog.common.result.PageResult;
import com.hunhiong.blog.common.result.Result;
import com.hunhiong.blog.dto.MusicDTO;
import com.hunhiong.blog.dto.MusicQueryDTO;
import com.hunhiong.blog.service.MusicService;
import com.hunhiong.blog.vo.MusicVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 音乐管理控制器
 *
 * @author hunhiong
 */
@Tag(name = "音乐管理")
@RestController
@RequestMapping("/music")
@RequiredArgsConstructor
public class MusicController {

    private final MusicService musicService;

    /**
     * 获取音乐列表
     */
    @Operation(summary = "获取音乐列表")
    @GetMapping("/list")
    public Result<List<MusicVO>> list() {
        return Result.success(musicService.list());
    }

    /**
     * 上传音乐文件
     */
    @Operation(summary = "上传音乐文件")
    @PostMapping("/upload")
    public Result<MusicVO> upload(
            @Parameter(description = "音乐文件", schema = @Schema(type = "string", format = "binary")) @RequestParam("file") MultipartFile file,
            @Parameter(description = "歌曲名称") @RequestParam("title") String title,
            @Parameter(description = "歌手") @RequestParam(value = "artist", required = false) String artist) {
        return Result.success(musicService.upload(file, title, artist));
    }

    /**
     * 分页查询音乐
     */
    @Operation(summary = "分页查询音乐")
    @GetMapping("/page")
    public Result<PageResult<MusicVO>> page(MusicQueryDTO queryDTO) {
        return Result.success(musicService.page(queryDTO));
    }

    /**
     * 查询音乐详情
     */
    @Operation(summary = "查询音乐详情")
    @GetMapping("/{id}")
    public Result<MusicVO> getById(@PathVariable Long id) {
        return Result.success(musicService.getById(id));
    }

    /**
     * 修改音乐信息
     */
    @Operation(summary = "修改音乐信息")
    @PutMapping("/update/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody MusicDTO dto) {
        musicService.update(id, dto);
        return Result.success();
    }

    /**
     * 批量删除音乐
     */
    @Operation(summary = "批量删除音乐")
    @DeleteMapping("/batch")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        musicService.batchDelete(ids);
        return Result.success();
    }
}
