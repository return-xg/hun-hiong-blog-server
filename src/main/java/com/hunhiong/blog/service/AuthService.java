package com.hunhiong.blog.service;

import com.hunhiong.blog.dto.LoginDTO;
import com.hunhiong.blog.dto.RegisterDTO;
import com.hunhiong.blog.vo.LoginVO;
import com.hunhiong.blog.vo.UserInfoVO;

/**
 * 认证服务接口
 *
 * <p>提供用户登录、注册、刷新 Token、获取当前用户信息等能力。</p>
 *
 * @author hunhiong
 */
public interface AuthService {

    /**
     * 用户登录
     *
     * @param loginDTO 登录请求参数
     * @return 登录返回信息（含 Token 与用户信息）
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 用户注册
     *
     * @param registerDTO 注册请求参数
     * @return 登录返回信息（注册后自动登录）
     */
    LoginVO register(RegisterDTO registerDTO);

    /**
     * 刷新 Token
     *
     * @param refreshToken 刷新令牌（Bearer token）
     * @return 登录返回信息（含新的 Token 与用户信息）
     */
    LoginVO refreshToken(String refreshToken);

    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    UserInfoVO getCurrentUserInfo();
}
