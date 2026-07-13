package com.hunhiong.blog.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 登录上下文工具
 *
 * <p>提供获取当前登录用户信息的便捷方法。</p>
 *
 * @author hunhiong
 */
@Slf4j
public final class JwtAuthContext {

    private JwtAuthContext() {
    }

    /**
     * 获取当前 Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取当前登录用户
     */
    public static LoginUser getLoginUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        return principal instanceof LoginUser loginUser ? loginUser : null;
    }

    /**
     * 获取当前用户ID
     */
    public static Long getCurrentUserId() {
        LoginUser loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getUserId();
    }

    /**
     * 获取当前用户名
     */
    public static String getCurrentUsername() {
        LoginUser loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getUsername();
    }

    /**
     * 获取当前昵称
     */
    public static String getCurrentNickname() {
        LoginUser loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getNickname();
    }

    /**
     * 是否已登录
     */
    public static boolean isLogin() {
        return getLoginUser() != null;
    }
}
