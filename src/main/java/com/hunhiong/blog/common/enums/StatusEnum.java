package com.hunhiong.blog.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用状态枚举（启用 / 禁用）
 *
 * @author hunhiong
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

    /** 禁用 */
    DISABLED(0, "禁用"),
    /** 启用 */
    ENABLED(1, "启用");

    private final int code;
    private final String desc;

    public boolean isEnabled() {
        return this == ENABLED;
    }
}
