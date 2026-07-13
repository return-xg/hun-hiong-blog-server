package com.hunhiong.blog.common.exception;

import lombok.Getter;

/**
 * 业务错误码枚举
 *
 * <p>用于业务逻辑中抛出 {@link BusinessException} 时指定具体错误码与消息。</p>
 *
 * @author hunhiong
 */
@Getter
public enum ErrorCode {

    /** 通用业务错误 */
    BUSINESS_ERROR(1000, "业务处理失败"),
    DATA_NOT_FOUND(1001, "数据不存在"),
    DATA_ALREADY_EXISTS(1002, "数据已存在"),
    DATA_OPERATION_FAILED(1003, "数据操作失败"),

    /** 鉴权相关 */
    TOKEN_INVALID(40101, "Token无效"),
    TOKEN_EXPIRED(40102, "Token已过期"),
    TOKEN_MISSING(40103, "Token缺失"),

    /** 用户相关 */
    USER_NOT_FOUND(2001, "用户不存在"),
    USER_PASSWORD_ERROR(2002, "用户名或密码错误"),
    USER_DISABLED(2003, "用户已被禁用"),
    USER_ALREADY_EXISTS(2004, "用户已存在"),
    USERNAME_OR_PASSWORD_EMPTY(2005, "用户名或密码不能为空"),
    USER_NOT_LOGIN(2006, "用户未登录"),

    /** 文章相关 */
    ARTICLE_NOT_FOUND(3001, "文章不存在"),
    ARTICLE_STATUS_ERROR(3002, "文章状态异常");

    /** 错误码 */
    private final int code;

    /** 错误消息 */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
