package com.hunhiong.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 重置用户密码请求 DTO（管理员操作）
 *
 * @author hunhiong
 */
@Data
public class ResetPasswordDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 新密码 */
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
    private String newPassword;
}
