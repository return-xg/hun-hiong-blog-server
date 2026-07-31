package com.hunhiong.blog.service.impl;

import com.hunhiong.blog.mapper.DashboardMapper;
import com.hunhiong.blog.service.DashboardService;
import com.hunhiong.blog.vo.CategoryDistributionVO;
import com.hunhiong.blog.vo.DashboardOverviewVO;
import com.hunhiong.blog.vo.DashboardRecentArticleVO;
import com.hunhiong.blog.vo.TagSimpleVO;
import com.hunhiong.blog.vo.TopArticleVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 仪表盘服务实现
 *
 * @author hunhiong
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DashboardMapper dashboardMapper;

    @Override
    public DashboardOverviewVO getOverview() {
        DashboardOverviewVO vo = new DashboardOverviewVO();
        vo.setArticleCount(dashboardMapper.countArticles());
        vo.setCategoryCount(dashboardMapper.countCategories());
        vo.setTagCount(dashboardMapper.countTags());
        vo.setViewCount(dashboardMapper.sumViewCount());
        vo.setLikeCount(dashboardMapper.sumLikeCount());
        return vo;
    }

    @Override
    public List<CategoryDistributionVO> getCategoryDistribution() {
        List<Map<String, Object>> dbResults = dashboardMapper.selectCategoryDistribution();
        List<CategoryDistributionVO> result = new ArrayList<>();
        for (Map<String, Object> row : dbResults) {
            CategoryDistributionVO vo = new CategoryDistributionVO();
            vo.setCategoryName((String) row.get("category_name"));
            vo.setArticleCount(((Number) row.get("article_count")).longValue());
            result.add(vo);
        }
        return result;
    }

    @Override
    public List<TopArticleVO> getTopArticles() {
        List<Map<String, Object>> dbResults = dashboardMapper.selectTopArticles();
        List<TopArticleVO> result = new ArrayList<>();
        for (Map<String, Object> row : dbResults) {
            TopArticleVO vo = new TopArticleVO();
            vo.setId(String.valueOf(row.get("id")));
            vo.setTitle((String) row.get("title"));
            vo.setViewCount(((Number) row.get("view_count")).longValue());
            result.add(vo);
        }
        return result;
    }

    @Override
    public List<DashboardRecentArticleVO> getRecentArticles() {
        List<Map<String, Object>> dbResults = dashboardMapper.selectRecentArticles();
        List<DashboardRecentArticleVO> result = new ArrayList<>();
        List<String> articleIds = new ArrayList<>();
        for (Map<String, Object> row : dbResults) {
            DashboardRecentArticleVO vo = new DashboardRecentArticleVO();
            String id = String.valueOf(row.get("id"));
            vo.setId(id);
            vo.setTitle((String) row.get("title"));
            vo.setCategoryName((String) row.get("category_name"));
            vo.setViewCount(((Number) row.get("view_count")).longValue());
            // 兼容 JDBC 返回 Timestamp 或 LocalDateTime
            Object createTimeObj = row.get("create_time");
            if (createTimeObj instanceof Timestamp ts) {
                vo.setCreateTime(ts.toLocalDateTime());
            } else if (createTimeObj instanceof LocalDateTime ldt) {
                vo.setCreateTime(ldt);
            }
            articleIds.add(id);
            result.add(vo);
        }

        // 批量查询文章关联的标签，按 article_id 分组后组装到 VO
        if (!articleIds.isEmpty()) {
            List<Map<String, Object>> tagResults = dashboardMapper.selectTagsByArticleIds(articleIds);
            Map<String, List<TagSimpleVO>> tagsByArticleId = new HashMap<>();
            for (Map<String, Object> tagRow : tagResults) {
                String articleId = (String) tagRow.get("article_id");
                TagSimpleVO tagVO = new TagSimpleVO();
                tagVO.setId(Long.parseLong((String) tagRow.get("tag_id")));
                tagVO.setName((String) tagRow.get("tag_name"));
                tagsByArticleId.computeIfAbsent(articleId, k -> new ArrayList<>()).add(tagVO);
            }
            for (DashboardRecentArticleVO vo : result) {
                vo.setTags(tagsByArticleId.getOrDefault(vo.getId(), Collections.emptyList()));
            }
        }

        return result;
    }
}
