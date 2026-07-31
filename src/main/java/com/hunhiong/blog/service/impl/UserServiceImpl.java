package com.hunhiong.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hunhiong.blog.common.constants.RedisConstants;
import com.hunhiong.blog.common.enums.StatusEnum;
import com.hunhiong.blog.common.exception.BusinessException;
import com.hunhiong.blog.common.exception.ErrorCode;
import com.hunhiong.blog.common.result.PageResult;
import com.hunhiong.blog.converter.SysUserConverter;
import com.hunhiong.blog.dto.*;
import com.hunhiong.blog.entity.SysUser;
import com.hunhiong.blog.mapper.SysUserMapper;
import com.hunhiong.blog.security.JwtAuthContext;
import com.hunhiong.blog.service.UserService;
import com.hunhiong.blog.utils.RedisService;
import com.hunhiong.blog.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 用户管理服务实现
 *
 * @author hunhiong
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SysUserMapper sysUserMapper;
    private final SysUserConverter sysUserConverter;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

    @Override
    public PageResult<UserVO> page(UserQueryDTO queryDTO) {
        // 构建分页参数
        Page<SysUser> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());

        // 构建查询条件：按用户名模糊搜索、按状态精确筛选
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(queryDTO.getUsername())) {
            wrapper.like(SysUser::getUsername, queryDTO.getUsername());
        }
        if (queryDTO.getStatus() != null) {
            wrapper.eq(SysUser::getStatus, queryDTO.getStatus());
        }
        wrapper.orderByDesc(SysUser::getCreateTime);

        Page<SysUser> result = sysUserMapper.selectPage(page, wrapper);

        // 转换为 VO 列表
        List<UserVO> voList = result.getRecords().stream()
                .map(sysUserConverter::toUserVO)
                .toList();

        return new PageResult<>(result.getCurrent(), result.getSize(), result.getTotal(), voList);
    }

    @Override
    public UserVO getById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return sysUserConverter.toUserVO(sysUser);
    }

    @Override
    public void update(Long id, UserDTO dto) {
        // 查询用户是否存在
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 获取当前操作者ID
        Long currentUserId = JwtAuthContext.getCurrentUserId();

        // 角色修改校验：仅超级管理员（id=1）可修改角色
        if (StringUtils.hasText(dto.getRole())) {
            if (currentUserId == null || currentUserId != 1L) {
                throw new BusinessException(ErrorCode.USER_ROLE_UPDATE_FORBIDDEN);
            }
            if (sysUser.getId() == 1L) {
                throw new BusinessException(ErrorCode.USER_SUPER_ADMIN_ROLE_PROTECTED);
            }
            if (!"admin".equals(dto.getRole()) && !"user".equals(dto.getRole())) {
                throw new BusinessException(ErrorCode.USER_ROLE_INVALID);
            }
            sysUser.setRole(dto.getRole());
        }

        // 状态修改校验：不允许禁用超级管理员和管理员
        if (dto.getStatus() != null) {
            if (sysUser.getId() == 1L) {
                throw new BusinessException(ErrorCode.USER_SUPER_ADMIN_DISABLE_FORBIDDEN);
            }
            if ("admin".equals(sysUser.getRole())) {
                throw new BusinessException(ErrorCode.USER_ADMIN_DISABLE_FORBIDDEN);
            }
            sysUser.setStatus(dto.getStatus());
        }

        // 更新非空字段
        if (StringUtils.hasText(dto.getNickname())) {
            sysUser.setNickname(dto.getNickname());
        }
        if (dto.getAvatar() != null) {
            sysUser.setAvatar(dto.getAvatar());
        }

        sysUserMapper.updateById(sysUser);

        log.info("修改用户信息成功: id={}", id);
    }

    @Override
    public void enable(Long id) {
        // 查询用户是否存在
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 更新状态为启用
        sysUser.setStatus(StatusEnum.ENABLED.getCode());
        sysUserMapper.updateById(sysUser);

        log.info("启用用户成功: id={}", id);
    }

    @Override
    public void disable(Long id) {
        // 查询用户是否存在
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 不允许禁用超级管理员
        if (sysUser.getId() == 1L) {
            throw new BusinessException(ErrorCode.USER_SUPER_ADMIN_DISABLE_FORBIDDEN);
        }
        // 不允许禁用管理员
        if ("admin".equals(sysUser.getRole())) {
            throw new BusinessException(ErrorCode.USER_ADMIN_DISABLE_FORBIDDEN);
        }

        // 更新状态为禁用
        sysUser.setStatus(StatusEnum.DISABLED.getCode());
        sysUserMapper.updateById(sysUser);

        // 清除该用户在 Redis 中的 Token，强制下线
        redisService.delete(RedisConstants.TOKEN_PREFIX + id);

        log.info("禁用用户成功: id={}", id);
    }

    @Override
    public void resetPassword(Long id, ResetPasswordDTO dto) {
        // 查询用户是否存在
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 加密新密码并更新
        sysUser.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        sysUserMapper.updateById(sysUser);

        // 清除该用户在 Redis 中的 Token，强制重新登录
        redisService.delete(RedisConstants.TOKEN_PREFIX + id);

        log.info("重置用户密码成功: id={}", id);
    }

    @Override
    public void delete(Long id) {
        // 不允许删除当前登录用户自己
        Long currentUserId = JwtAuthContext.getCurrentUserId();
        if (currentUserId != null && currentUserId.equals(id)) {
            throw new BusinessException(ErrorCode.USER_CANNOT_DELETE_SELF);
        }

        // 查询用户是否存在
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 不允许删除超级管理员
        if (sysUser.getId() == 1L) {
            throw new BusinessException(ErrorCode.USER_SUPER_ADMIN_DELETE_FORBIDDEN);
        }
        // 不允许删除管理员
        if ("admin".equals(sysUser.getRole())) {
            throw new BusinessException(ErrorCode.USER_ADMIN_DELETE_FORBIDDEN);
        }

        // 物理删除用户
        sysUserMapper.deleteById(id);

        // 清除该用户在 Redis 中的 Token
        redisService.delete(RedisConstants.TOKEN_PREFIX + id);

        log.info("删除用户成功: id={}", id);
    }

    @Override
    public void updateProfile(UpdateProfileDTO dto) {
        // 获取当前登录用户ID
        Long currentUserId = JwtAuthContext.getCurrentUserId();
        SysUser sysUser = sysUserMapper.selectById(currentUserId);
        if (sysUser == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 更新非空字段
        if (StringUtils.hasText(dto.getNickname())) {
            sysUser.setNickname(dto.getNickname());
        }
        if (dto.getAvatar() != null) {
            sysUser.setAvatar(dto.getAvatar());
        }

        sysUserMapper.updateById(sysUser);

        log.info("修改个人信息成功: userId={}", currentUserId);
    }

    @Override
    public void changePassword(ChangePasswordDTO dto) {
        // 获取当前登录用户
        Long currentUserId = JwtAuthContext.getCurrentUserId();
        SysUser sysUser = sysUserMapper.selectById(currentUserId);
        if (sysUser == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 校验旧密码是否正确
        if (!passwordEncoder.matches(dto.getOldPassword(), sysUser.getPassword())) {
            throw new BusinessException(ErrorCode.USER_OLD_PASSWORD_ERROR);
        }

        // 更新密码
        sysUser.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        sysUserMapper.updateById(sysUser);

        // 清除 Redis 中的 Token，强制重新登录
        redisService.delete(RedisConstants.TOKEN_PREFIX + currentUserId);

        log.info("修改密码成功: userId={}", currentUserId);
    }
}
