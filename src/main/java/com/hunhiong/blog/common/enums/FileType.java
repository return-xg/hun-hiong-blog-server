package com.hunhiong.blog.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件类型枚举
 *
 * <p>定义文件上传的类型分类，用于区分不同类型的文件校验和存储路径。</p>
 *
 * @author hunhiong
 */
@Getter
@AllArgsConstructor
public enum FileType {

    /** 图片文件 */
    IMAGE("image", "图片"),

    /** 音乐文件 */
    MUSIC("music", "音乐");

    /** 存储路径前缀 */
    private final String pathPrefix;

    /** 描述 */
    private final String desc;
}
