package com.hunhiong.blog.service.impl;

import com.hunhiong.blog.mapper.DashboardMapper;
import com.hunhiong.blog.service.DashboardService;
import com.hunhiong.blog.vo.DashboardOverviewVO;
import com.hunhiong.blog.vo.DashboardRecentArticleVO;
import com.hunhiong.blog.vo.DashboardTrendVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    public List<DashboardTrendVO> getTrend() {
        // 计算 7 天前的零点作为起始时间
        LocalDateTime startTime = LocalDate.now().minusDays(6).atStartOfDay();

        // 查询数据库中有数据的日期
        List<Map<String, Object>> dbResults = dashboardMapper.selectDailyViewCount(startTime);

        // 将数据库结果转为日期 → 浏览量的映射
        Map<String, Long> viewCountMap = new HashMap<>();
        for (Map<String, Object> row : dbResults) {
            String date = (String) row.get("date");
            long viewCount = ((Number) row.get("view_count")).longValue();
            viewCountMap.put(date, viewCount);
        }

        // 生成完整的 7 天列表，无数据的日期补 0
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        List<DashboardTrendVO> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = LocalDate.now().minusDays(6 - i);
            String dateStr = date.format(formatter);
            DashboardTrendVO trendVO = new DashboardTrendVO();
            trendVO.setDate(dateStr);
            trendVO.setViewCount(viewCountMap.getOrDefault(dateStr, 0L));
            result.add(trendVO);
        }
        return result;
    }

    @Override
    public List<DashboardRecentArticleVO> getRecentArticles() {
        List<Map<String, Object>> dbResults = dashboardMapper.selectRecentArticles();
        List<DashboardRecentArticleVO> result = new ArrayList<>();
        for (Map<String, Object> row : dbResults) {
            DashboardRecentArticleVO vo = new DashboardRecentArticleVO();
            vo.setId(String.valueOf(row.get("id")));
            vo.setTitle((String) row.get("title"));
            vo.setCategoryName((String) row.get("category_name"));
            vo.setViewCount(((Number) row.get("view_count")).longValue());
            Object createTimeObj = row.get("create_time");
            if (createTimeObj instanceof LocalDateTime ldt) {
                vo.setCreateTime(ldt);
            }
            result.add(vo);
        }
        return result;
    }
}
