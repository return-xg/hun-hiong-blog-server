package com.hunhiong.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 修改个人密码请求 DTO（用户自助操作）
 *
 * @author hunhiong
 */
@Data
public class ChangePasswordDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 旧密码 */
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    /** 新密码 */
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}
