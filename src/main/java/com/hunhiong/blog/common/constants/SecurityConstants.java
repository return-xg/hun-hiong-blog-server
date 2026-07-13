package com.hunhiong.blog.common.constants;

/**
 * 安全相关常量
 *
 * @author hunhiong
 */
public final class SecurityConstants {

    private SecurityConstants() {
    }

    /** Token 请求头名称 */
    public static final String TOKEN_HEADER = "Authorization";

    /** Token 前缀 */
    public static final String TOKEN_PREFIX = "Bearer ";

    /** JWT 中用户ID的 claims key */
    public static final String CLAIM_USER_ID = "userId";

    /** JWT 中用户名的 claims key */
    public static final String CLAIM_USERNAME = "username";

    /** JWT 中昵称的 claims key */
    public static final String CLAIM_NICKNAME = "nickname";

    /** 登录用户在 SecurityContext 中的权限角色前缀 */
    public static final String ROLE_PREFIX = "ROLE_";

    /** 默认管理员角色 */
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    /** 默认普通用户角色 */
    public static final String ROLE_USER = "ROLE_USER";
}
