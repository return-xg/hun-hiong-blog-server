package com.hunhiong.blog.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 标签简要信息 VO
 *
 * <p>仅包含标签ID和名称，用于文章关联标签的轻量展示。</p>
 *
 * @author hunhiong
 */
@Data
public class TagSimpleVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 标签ID */
    private Long id;

    /** 标签名称 */
    private String name;
}
