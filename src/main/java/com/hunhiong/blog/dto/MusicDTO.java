package com.hunhiong.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 音乐请求 DTO（新增 / 修改通用）
 *
 * <p>新增时 title 为必填，修改时所有字段均为可选。</p>
 *
 * @author hunhiong
 */
@Data
public class MusicDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 歌曲名称 */
    @NotBlank(groups = Create.class, message = "歌曲名称不能为空")
    @Size(max = 128, message = "歌曲名称长度不能超过128")
    private String title;

    /** 歌手 */
    @Size(max = 128, message = "歌手名称长度不能超过128")
    private String artist;

    /** 封面地址 */
    @Size(max = 512, message = "封面地址长度不能超过512")
    private String cover;

    /** 歌曲时长（秒） */
    private Integer duration;

    /** 排序值 */
    private Integer sort;

    /** 状态：1-启用，0-禁用 */
    private Integer status;

    /**
     * 新增校验分组标记
     */
    public interface Create {
    }
}
