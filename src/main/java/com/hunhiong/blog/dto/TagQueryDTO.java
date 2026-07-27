package com.hunhiong.blog.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 标签分页查询请求 DTO
 *
 * @author hunhiong
 */
@Data
public class TagQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 标签名称（模糊搜索） */
    private String name;

    /** 当前页，默认1 */
    private Integer current = 1;

    /** 每页大小，默认10 */
    private Integer size = 10;
}
