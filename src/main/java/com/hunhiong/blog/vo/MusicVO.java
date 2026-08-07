package com.hunhiong.blog.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 音乐信息 VO
 *
 * <p>返回前端播放器所需的歌曲基本信息。</p>
 *
 * @author hunhiong
 */
@Data
public class MusicVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 音乐ID */
    private Long id;

    /** 歌曲名称 */
    private String title;

    /** 歌手 */
    private String artist;

    /** 封面地址 */
    private String cover;

    /** 音乐文件地址 */
    private String url;

    /** 歌曲时长（秒） */
    private Integer duration;

    /** 排序值 */
    private Integer sort;

    /** 状态：1-启用，0-禁用 */
    private Integer status;
}
