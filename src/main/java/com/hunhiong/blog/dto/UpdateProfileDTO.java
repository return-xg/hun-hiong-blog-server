package com.hunhiong.blog.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 修改个人信息请求 DTO（用户自助操作）
 *
 * @author hunhiong
 */
@Data
public class UpdateProfileDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 昵称 */
    @Size(max = 64, message = "昵称长度不能超过64")
    private String nickname;

    /** 头像URL */
    @Size(max = 255, message = "头像URL长度不能超过255")
    private String avatar;
}
