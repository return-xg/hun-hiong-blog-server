package com.hunhiong.blog.common.result;

import lombok.Getter;

/**
 * 通用返回码枚举（基础码）
 *
 * <p>仅包含通用结果码，业务错误码见 {@code com.hunhiong.blog.common.exception.ErrorCode}</p>
 *
 * @author hunhiong
 */
@Getter
public enum ResultCode {

    /** 成功 */
    SUCCESS(0, "success"),

    /** 通用失败 */
    FAILED(500, "操作失败"),

    /** 参数错误 */
    PARAM_ERROR(400, "参数错误"),
    PARAM_VALIDATE_FAILED(40001, "参数校验失败"),

    /** 未登录 / 未授权 */
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "无权限访问"),

    /** 资源不存在 */
    NOT_FOUND(404, "资源不存在"),

    /** 系统错误 */
    SYSTEM_ERROR(500, "系统异常"),
    SERVICE_UNAVAILABLE(503, "服务暂不可用");

    /** 返回码 */
    private final int code;

    /** 返回消息 */
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
