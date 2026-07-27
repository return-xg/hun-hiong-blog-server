package com.hunhiong.blog.common.enums;

import com.hunhiong.blog.common.exception.BusinessException;
import com.hunhiong.blog.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文章状态枚举
 *
 * @author hunhiong
 */
@Getter
@AllArgsConstructor
public enum ArticleStatusEnum {

    /** 草稿 */
    DRAFT(0, "草稿"),
    /** 已发布 */
    PUBLISHED(1, "已发布"),
    /** 下线 */
    OFFLINE(2, "下线");

    private final int code;
    private final String desc;

    /**
     * 根据 code 获取对应枚举
     *
     * @param code 状态码
     * @return 文章状态枚举
     * @throws BusinessException 当 code 无法匹配时抛出
     */
    public static ArticleStatusEnum of(int code) {
        for (ArticleStatusEnum status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new BusinessException(ErrorCode.ARTICLE_STATUS_ERROR);
    }
}
