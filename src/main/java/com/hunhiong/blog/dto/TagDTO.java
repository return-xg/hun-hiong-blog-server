package com.hunhiong.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 标签请求 DTO（新增 / 修改通用）
 *
 * <p>新增时 name 为必填，修改时所有字段均为可选。</p>
 *
 * @author hunhiong
 */
@Data
public class TagDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 标签名称 */
    @NotBlank(groups = Create.class, message = "标签名称不能为空")
    @Size(max = 64, message = "标签名称长度不能超过64")
    private String name;

    /** 标签别名（URL友好） */
    @Size(max = 128, message = "标签别名长度不能超过128")
    private String slug;

    /**
     * 新增校验分组标记
     */
    public interface Create {
    }
}
