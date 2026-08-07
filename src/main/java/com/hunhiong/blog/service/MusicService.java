package com.hunhiong.blog.service;

import com.hunhiong.blog.common.result.PageResult;
import com.hunhiong.blog.dto.MusicDTO;
import com.hunhiong.blog.dto.MusicQueryDTO;
import com.hunhiong.blog.vo.MusicVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 音乐管理服务接口
 *
 * <p>提供音乐列表查询、上传等能力，为前端播放器提供数据支持。</p>
 *
 * @author hunhiong
 */
public interface MusicService {

    /**
     * 获取启用的音乐列表（按 sort 升序）
     *
     * @return 音乐列表
     */
    List<MusicVO> list();

    /**
     * 上传音乐文件
     *
     * @param file   音乐文件
     * @param title  歌曲名称
     * @param artist 歌手
     * @return 音乐信息
     */
    MusicVO upload(MultipartFile file, String title, String artist);

    /**
     * 分页查询音乐
     *
     * @param queryDTO 分页查询参数
     * @return 分页结果
     */
    PageResult<MusicVO> page(MusicQueryDTO queryDTO);

    /**
     * 根据ID查询音乐详情
     *
     * @param id 音乐ID
     * @return 音乐信息
     */
    MusicVO getById(Long id);

    /**
     * 修改音乐信息
     *
     * @param id  音乐ID
     * @param dto 音乐请求参数
     */
    void update(Long id, MusicDTO dto);

    /**
     * 批量删除音乐（逻辑删除）
     *
     * @param ids 音乐ID列表
     */
    void batchDelete(List<Long> ids);
}
