package com.hunhiong.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hunhiong.blog.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户 Mapper
 *
 * @author hunhiong
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
