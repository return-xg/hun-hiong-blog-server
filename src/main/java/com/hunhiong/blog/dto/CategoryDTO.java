package com.hunhiong.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分类请求 DTO（新增 / 修改通用）
 *
 * <p>新增时 name 为必填，修改时所有字段均为可选。</p>
 *
 * @author hunhiong
 */
@Data
public class CategoryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 分类名称 */
    @NotBlank(groups = Create.class, message = "分类名称不能为空")
    @Size(max = 64, message = "分类名称长度不能超过64")
    private String name;

    /** 分类别名（URL友好） */
    @Size(max = 128, message = "分类别名长度不能超过128")
    private String slug;

    /** 排序（升序），默认0 */
    private Integer sort = 0;

    /** 分类描述 */
    @Size(max = 255, message = "分类描述长度不能超过255")
    private String description;

    /**
     * 新增校验分组标记
     */
    public interface Create {
    }
}
