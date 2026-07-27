package com.hunhiong.blog.task;

import com.hunhiong.blog.common.constants.RedisConstants;
import com.hunhiong.blog.mapper.BlogArticleMapper;
import com.hunhiong.blog.utils.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 文章统计数据同步定时任务
 *
 * <p>定时将 Redis 中累计的浏览量/点赞量增量同步到数据库，避免频繁写库。</p>
 *
 * @author hunhiong
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleStatsSyncTask {

    private final RedisService redisService;
    private final BlogArticleMapper blogArticleMapper;

    /**
     * 每 5 分钟同步一次文章浏览量增量到数据库
     */
    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void syncViewCounts() {
        syncDelta(RedisConstants.ARTICLE_VIEW_DELTA_HASH, "浏览量");
    }

    /**
     * 每 5 分钟同步一次文章点赞量增量到数据库
     */
    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void syncLikeCounts() {
        syncDelta(RedisConstants.ARTICLE_LIKE_DELTA_HASH, "点赞量");
    }

    /**
     * 同步指定 Hash 中的增量数据到数据库
     *
     * @param hashKey Redis Hash Key
     * @param label   日志标签（浏览量/点赞量）
     */
    private void syncDelta(String hashKey, String label) {
        // 读取 Hash 中所有增量数据
        Map<Object, Object> deltaMap = redisService.getHashEntries(hashKey);
        if (deltaMap.isEmpty()) {
            return;
        }

        log.info("开始同步文章{}增量, 共 {} 条", label, deltaMap.size());

        int successCount = 0;
        for (Map.Entry<Object, Object> entry : deltaMap.entrySet()) {
            Long articleId = Long.valueOf(entry.getKey().toString());
            long delta = ((Number) entry.getValue()).longValue();
            if (delta <= 0) {
                continue;
            }

            int rows;
            if (RedisConstants.ARTICLE_VIEW_DELTA_HASH.equals(hashKey)) {
                rows = blogArticleMapper.incrementViewCount(articleId, delta);
            } else {
                rows = blogArticleMapper.incrementLikeCount(articleId, delta);
            }

            if (rows > 0) {
                successCount++;
            } else {
                log.warn("文章{}增量同步失败, articleId={}, delta={}, 文章可能已删除", label, articleId, delta);
            }
        }

        // 同步完成后删除 Hash，重新累计
        redisService.delete(hashKey);
        log.info("文章{}增量同步完成, 成功 {}/{} 条", label, successCount, deltaMap.size());
    }
}
