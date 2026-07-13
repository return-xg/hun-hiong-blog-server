package com.hunhiong.blog.common.constants;

/**
 * Redis Key 常量
 *
 * @author hunhiong
 */
public final class RedisConstants {

    private RedisConstants() {
    }

    /** 项目统一前缀 */
    public static final String PREFIX = "blog:";

    /** Token 前缀  blog:token:{userId} */
    public static final String TOKEN_PREFIX = PREFIX + "token:";

    /** 登录用户信息前缀  blog:user:info:{userId} */
    public static final String USER_INFO_PREFIX = PREFIX + "user:info:";

    /** 登录验证码前缀  blog:captcha:{key} */
    public static final String CAPTCHA_PREFIX = PREFIX + "captcha:";

    /** 文章浏览量前缀  blog:article:view:{articleId} */
    public static final String ARTICLE_VIEW_PREFIX = PREFIX + "article:view:";

    /** 文章点赞量前缀  blog:article:like:{articleId} */
    public static final String ARTICLE_LIKE_PREFIX = PREFIX + "article:like:";

    /** 默认过期时间：30 分钟（秒） */
    public static final long DEFAULT_EXPIRE = 30 * 60L;

    /** Token 默认过期时间：2 小时（秒） */
    public static final long TOKEN_EXPIRE = 2 * 60 * 60L;

    /** 验证码过期时间：5 分钟（秒） */
    public static final long CAPTCHA_EXPIRE = 5 * 60L;
}
