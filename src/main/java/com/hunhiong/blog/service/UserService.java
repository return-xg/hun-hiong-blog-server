package com.hunhiong.blog.service;

import com.hunhiong.blog.common.result.PageResult;
import com.hunhiong.blog.dto.*;
import com.hunhiong.blog.vo.UserVO;

/**
 * 用户管理服务接口
 *
 * <p>提供后台管理端（用户列表、详情、修改、启用/禁用、重置密码、删除）
 * 以及用户自助端（修改个人信息、修改密码）能力。</p>
 *
 * @author hunhiong
 */
public interface UserService {

    /**
     * 分页查询用户列表（管理端）
     *
     * @param queryDTO 分页查询参数
     * @return 分页结果
     */
    PageResult<UserVO> page(UserQueryDTO queryDTO);

    /**
     * 查询用户详情（管理端）
     *
     * @param id 用户ID
     * @return 用户信息
     */
    UserVO getById(Long id);

    /**
     * 修改用户信息（管理端）
     *
     * @param id  用户ID
     * @param dto 修改请求参数
     */
    void update(Long id, UserDTO dto);

    /**
     * 启用用户（管理端）
     *
     * @param id 用户ID
     */
    void enable(Long id);

    /**
     * 禁用用户（管理端），同时清除该用户 Redis Token
     *
     * @param id 用户ID
     */
    void disable(Long id);

    /**
     * 重置用户密码（管理端）
     *
     * @param id  用户ID
     * @param dto 重置密码请求参数
     */
    void resetPassword(Long id, ResetPasswordDTO dto);

    /**
     * 删除用户（管理端，物理删除），不允许删除自己
     *
     * @param id 用户ID
     */
    void delete(Long id);

    /**
     * 修改个人信息（用户自助端）
     *
     * @param dto 修改个人信息请求参数
     */
    void updateProfile(UpdateProfileDTO dto);

    /**
     * 修改个人密码（用户自助端），成功后清除 Redis Token 强制重新登录
     *
     * @param dto 修改密码请求参数
     */
    void changePassword(ChangePasswordDTO dto);
}
