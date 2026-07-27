package com.hunhiong.blog.controller;

import com.hunhiong.blog.common.result.PageResult;
import com.hunhiong.blog.common.result.Result;
import com.hunhiong.blog.dto.*;
import com.hunhiong.blog.service.UserService;
import com.hunhiong.blog.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 *
 * <p>包含后台管理端（管理员管理所有用户）和用户自助端（当前登录用户修改自己的信息和密码）。</p>
 *
 * @author hunhiong
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ==================== 后台管理接口 ====================

    /**
     * 分页查询用户列表（管理端）
     */
    @Operation(summary = "分页查询用户列表")
    @GetMapping("/page")
    public Result<PageResult<UserVO>> page(UserQueryDTO queryDTO) {
        return Result.success(userService.page(queryDTO));
    }

    /**
     * 查询用户详情（管理端）
     */
    @Operation(summary = "查询用户详情")
    @GetMapping("/{id}")
    public Result<UserVO> getById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    /**
     * 修改用户信息（管理端）
     */
    @Operation(summary = "修改用户信息")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Validated(UserDTO.Create.class) @RequestBody UserDTO dto) {
        userService.update(id, dto);
        return Result.success();
    }

    /**
     * 启用用户（管理端）
     */
    @Operation(summary = "启用用户")
    @PutMapping("/{id}/enable")
    public Result<Void> enable(@PathVariable Long id) {
        userService.enable(id);
        return Result.success();
    }

    /**
     * 禁用用户（管理端）
     */
    @Operation(summary = "禁用用户")
    @PutMapping("/{id}/disable")
    public Result<Void> disable(@PathVariable Long id) {
        userService.disable(id);
        return Result.success();
    }

    /**
     * 重置用户密码（管理端）
     */
    @Operation(summary = "重置用户密码")
    @PutMapping("/{id}/reset-password")
    public Result<Void> resetPassword(@PathVariable Long id, @Valid @RequestBody ResetPasswordDTO dto) {
        userService.resetPassword(id, dto);
        return Result.success();
    }

    /**
     * 删除用户（管理端，物理删除）
     */
    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.success();
    }

    // ==================== 用户自助接口 ====================

    /**
     * 修改个人信息（用户自助端）
     */
    @Operation(summary = "修改个人信息")
    @PutMapping("/profile")
    public Result<Void> updateProfile(@Valid @RequestBody UpdateProfileDTO dto) {
        userService.updateProfile(dto);
        return Result.success();
    }

    /**
     * 修改个人密码（用户自助端）
     */
    @Operation(summary = "修改个人密码")
    @PutMapping("/password")
    public Result<Void> changePassword(@Valid @RequestBody ChangePasswordDTO dto) {
        userService.changePassword(dto);
        return Result.success();
    }
}
