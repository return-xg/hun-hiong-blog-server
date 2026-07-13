package com.hunhiong.blog.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否删除枚举（逻辑删除标识）
 *
 * @author hunhiong
 */
@Getter
@AllArgsConstructor
public enum DeletedEnum {

    /** 未删除 */
    NOT_DELETED(0, "未删除"),
    /** 已删除 */
    DELETED(1, "已删除");

    private final int code;
    private final String desc;
}
