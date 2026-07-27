package com.hunhiong.blog.converter;

import com.hunhiong.blog.entity.SysUser;
import com.hunhiong.blog.vo.UserVO;
import org.springframework.stereotype.Component;

/**
 * 系统用户对象转换器
 *
 * <p>负责 SysUser Entity 与 UserVO 之间的转换，password 字段不映射到 VO。</p>
 *
 * @author hunhiong
 */
@Component
public class SysUserConverter {

    /**
     * SysUser Entity 转 UserVO
     *
     * @param sysUser 用户实体
     * @return 用户信息 VO，入参为 null 时返回 null
     */
    public UserVO toUserVO(SysUser sysUser) {
        if (sysUser == null) {
            return null;
        }
        UserVO vo = new UserVO();
        vo.setId(sysUser.getId());
        vo.setUsername(sysUser.getUsername());
        vo.setNickname(sysUser.getNickname());
        vo.setAvatar(sysUser.getAvatar());
        vo.setStatus(sysUser.getStatus());
        vo.setCreateTime(sysUser.getCreateTime());
        return vo;
    }
}
