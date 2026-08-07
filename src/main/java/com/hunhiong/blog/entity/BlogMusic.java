package com.hunhiong.blog.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 博客音乐实体
 *
 * <p>映射 blog_music 表，支持逻辑删除。</p>
 *
 * @author hunhiong
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("blog_music")
public class BlogMusic extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 关联文件ID */
    private Long fileId;

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

    /** 排序（升序） */
    private Integer sort;

    /** 状态：1-启用，0-禁用 */
    private Integer status;

    /** 逻辑删除：0-未删除，1-已删除 */
    @TableLogic
    private Integer deleted;
}
