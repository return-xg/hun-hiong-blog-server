package com.hunhiong.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hunhiong.blog.common.constants.RedisConstants;
import com.hunhiong.blog.common.enums.StatusEnum;
import com.hunhiong.blog.common.exception.BusinessException;
import com.hunhiong.blog.common.exception.ErrorCode;
import com.hunhiong.blog.converter.SysUserConverter;
import com.hunhiong.blog.dto.LoginDTO;
import com.hunhiong.blog.dto.RegisterDTO;
import com.hunhiong.blog.entity.SysUser;
import com.hunhiong.blog.mapper.SysUserMapper;
import com.hunhiong.blog.security.JwtAuthContext;
import com.hunhiong.blog.security.JwtTokenProvider;
import com.hunhiong.blog.service.AuthService;
import com.hunhiong.blog.utils.RedisService;
import com.hunhiong.blog.vo.LoginVO;
import com.hunhiong.blog.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证服务实现
 *
 * @author hunhiong
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper sysUserMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final SysUserConverter sysUserConverter;

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 根据用户名查询用户
        SysUser sysUser = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, loginDTO.getUsername()));

        // 用户不存在时统一返回"用户名或密码错误"，防止信息泄露
        if (sysUser == null) {
            throw new BusinessException(ErrorCode.USER_PASSWORD_ERROR);
        }

        // 密码校验
        if (!passwordEncoder.matches(loginDTO.getPassword(), sysUser.getPassword())) {
            throw new BusinessException(ErrorCode.USER_PASSWORD_ERROR);
        }

        // 状态校验：禁用用户不允许登录
        if (sysUser.getStatus() == null || sysUser.getStatus() != StatusEnum.ENABLED.getCode()) {
            throw new BusinessException(ErrorCode.USER_DISABLED);
        }

        // 生成 Access Token 与 Refresh Token
        return generateAndSaveTokens(sysUser);
    }

    @Override
    public LoginVO register(RegisterDTO registerDTO) {
        // 检查用户名是否已存在
        SysUser existingUser = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, registerDTO.getUsername()));
        if (existingUser != null) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS);
        }

        // 构建用户实体并入库
        SysUser sysUser = new SysUser();
        sysUser.setUsername(registerDTO.getUsername());
        sysUser.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        sysUser.setNickname(registerDTO.getNickname());
        sysUser.setStatus(StatusEnum.ENABLED.getCode());
        sysUserMapper.insert(sysUser);

        log.info("用户注册成功: username={}, userId={}", sysUser.getUsername(), sysUser.getId());

        // 注册后自动登录，生成 Token
        return generateAndSaveTokens(sysUser);
    }

    @Override
    public LoginVO refreshToken(String refreshToken) {
        // 从 Bearer 头中提取 Token
        String token = jwtTokenProvider.resolveToken(refreshToken);
        if (token == null) {
            throw new BusinessException(ErrorCode.TOKEN_MISSING);
        }

        // 校验 Refresh Token 有效性
        if (!jwtTokenProvider.validateToken(token)) {
            throw new BusinessException(ErrorCode.TOKEN_INVALID);
        }

        // 从 Token 中获取用户信息
        Long userId = jwtTokenProvider.getUserId(token);
        String username = jwtTokenProvider.getUsername(token);

        // 查询用户最新信息，确保用户仍然存在且可用
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        if (sysUser.getStatus() == null || sysUser.getStatus() != StatusEnum.ENABLED.getCode()) {
            throw new BusinessException(ErrorCode.USER_DISABLED);
        }

        log.info("刷新 Token: userId={}, username={}", userId, username);

        // 生成新的 Access Token 与 Refresh Token
        return generateAndSaveTokens(sysUser);
    }

    @Override
    public UserInfoVO getCurrentUserInfo() {
        // 从安全上下文获取当前用户ID
        Long currentUserId = JwtAuthContext.getCurrentUserId();
        if (currentUserId == null) {
            throw new BusinessException(ErrorCode.USER_NOT_LOGIN);
        }

        // 查询用户信息
        SysUser sysUser = sysUserMapper.selectById(currentUserId);
        if (sysUser == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        return sysUserConverter.sysUserToUserInfoVO(sysUser);
    }

    /**
     * 生成 Access Token 与 Refresh Token，并将 Access Token 存入 Redis
     *
     * @param sysUser 用户实体
     * @return 登录返回信息
     */
    private LoginVO generateAndSaveTokens(SysUser sysUser) {
        // 生成 Token
        String accessToken = jwtTokenProvider.generateAccessToken(
                sysUser.getId(), sysUser.getUsername(), sysUser.getNickname());
        String refreshToken = jwtTokenProvider.generateRefreshToken(
                sysUser.getId(), sysUser.getUsername(), sysUser.getNickname());

        // 将 Access Token 存入 Redis，key = blog:token:{userId}
        String redisKey = RedisConstants.TOKEN_PREFIX + sysUser.getId();
        redisService.set(redisKey, accessToken, jwtTokenProvider.getAccessTokenExpiration());

        // 构建返回对象
        LoginVO loginVO = new LoginVO();
        loginVO.setAccessToken(accessToken);
        loginVO.setRefreshToken(refreshToken);
        loginVO.setExpiresIn(jwtTokenProvider.getAccessTokenExpiration());
        loginVO.setUserInfo(sysUserConverter.sysUserToUserInfoVO(sysUser));

        return loginVO;
    }
}
