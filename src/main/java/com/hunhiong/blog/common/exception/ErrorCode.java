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
    USER_CANNOT_DELETE_SELF(2007, "不能删除当前登录用户"),
    USER_OLD_PASSWORD_ERROR(2008, "旧密码错误"),

    /** 文章相关 */
    ARTICLE_NOT_FOUND(3001, "文章不存在"),
    ARTICLE_STATUS_ERROR(3002, "文章状态异常"),
    ARTICLE_TITLE_EXISTS(3003, "文章标题已存在"),

    /** 分类相关 */
    CATEGORY_NOT_FOUND(3101, "分类不存在"),
    CATEGORY_NAME_EXISTS(3102, "分类名称已存在"),
    CATEGORY_SLUG_EXISTS(3103, "分类别名已存在"),
    CATEGORY_HAS_ARTICLES(3104, "分类下存在文章，无法删除"),

    /** 标签相关 */
    TAG_NOT_FOUND(3201, "标签不存在"),
    TAG_NAME_EXISTS(3202, "标签名称已存在"),
    TAG_SLUG_EXISTS(3203, "标签别名已存在"),
    TAG_HAS_ARTICLES(3204, "标签下存在文章，无法删除"),

    /** 文件相关 */
    FILE_TYPE_NOT_ALLOWED(5001, "文件类型不支持"),
    FILE_SIZE_EXCEEDED(5002, "文件大小超出限制"),
    FILE_UPLOAD_FAILED(5003, "文件上传失败"),
    FILE_NOT_FOUND(5004, "文件不存在");

    /** 错误码 */
    private final int code;

    /** 错误消息 */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
