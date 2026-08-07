package com.hunhiong.blog.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 音乐分页查询请求 DTO
 *
 * @author hunhiong
 */
@Data
public class MusicQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 歌曲名称（模糊搜索） */
    private String title;

    /** 歌手（模糊搜索） */
    private String artist;

    /** 状态：1-启用，0-禁用 */
    private Integer status;

    /** 当前页，默认1 */
    private Integer current = 1;

    /** 每页大小，默认10 */
    private Integer size = 10;
}
