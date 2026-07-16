package com.hunhiong.blog.converter;

import com.hunhiong.blog.entity.SysUser;
import com.hunhiong.blog.vo.UserInfoVO;
import org.springframework.stereotype.Component;

/**
 * 系统用户对象转换器
 *
 * <p>负责 SysUser Entity 与 UserInfoVO 之间的转换，password 字段不映射到 VO。</p>
 *
 * @author hunhiong
 */
@Component
public class SysUserConverter {

    /**
     * SysUser Entity 转 UserInfoVO
     *
     * @param sysUser 用户实体
     * @return 用户信息 VO，入参为 null 时返回 null
     */
    public UserInfoVO sysUserToUserInfoVO(SysUser sysUser) {
        if (sysUser == null) {
            return null;
        }
        UserInfoVO vo = new UserInfoVO();
        vo.setId(sysUser.getId());
        vo.setUsername(sysUser.getUsername());
        vo.setNickname(sysUser.getNickname());
        vo.setAvatar(sysUser.getAvatar());
        vo.setStatus(sysUser.getStatus());
        return vo;
    }
}
