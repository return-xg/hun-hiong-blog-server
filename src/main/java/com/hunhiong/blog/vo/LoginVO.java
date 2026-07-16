package com.hunhiong.blog.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录返回 VO
 *
 * <p>包含访问令牌、刷新令牌、过期时间及用户基本信息。</p>
 *
 * @author hunhiong
 */
@Data
public class LoginVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Access Token */
    private String accessToken;

    /** Refresh Token */
    private String refreshToken;

    /** Access Token 过期时间（秒） */
    private long expiresIn;

    /** 用户信息 */
    private UserInfoVO userInfo;
}
