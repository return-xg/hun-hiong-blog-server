package com.hunhiong.blog.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户分页查询请求 DTO
 *
 * @author hunhiong
 */
@Data
public class UserQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 用户名（模糊搜索） */
    private String username;

    /** 状态：1-启用，0-禁用（精确筛选） */
    private Integer status;

    /** 当前页，默认1 */
    private Integer current = 1;

    /** 每页大小，默认10 */
    private Integer size = 10;
}
