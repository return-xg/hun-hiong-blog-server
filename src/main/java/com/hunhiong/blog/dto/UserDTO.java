package com.hunhiong.blog.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 修改用户信息请求 DTO（管理员操作）
 *
 * @author hunhiong
 */
@Data
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 昵称 */
    @Size(max = 64, message = "昵称长度不能超过64")
    private String nickname;

    /** 头像URL */
    @Size(max = 255, message = "头像URL长度不能超过255")
    private String avatar;

    /** 状态：1-启用，0-禁用 */
    private Integer status;

    /**
     * 新增校验分组标记
     */
    public interface Create {
    }
}
