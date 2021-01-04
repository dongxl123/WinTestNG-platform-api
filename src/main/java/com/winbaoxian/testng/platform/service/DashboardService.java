package com.winbaoxian.testng.platform.service;

import com.winbaoxian.testng.model.entity.ProjectQualityReportHistoryEntity;
import com.winbaoxian.testng.platform.model.common.Pagination;
import com.winbaoxian.testng.platform.model.common.PaginationDTO;
import com.winbaoxian.testng.platform.model.constant.WinTestNGPlatformConstant;
import com.winbaoxian.testng.platform.model.dto.*;
import com.winbaoxian.testng.platform.model.mapper.PlatformProjectQualityReportHistoryMapper;
import com.winbaoxian.testng.platform.repository.PlatformDashboardRepository;
import com.winbaoxian.testng.platform.repository.PlatformProjectQualityReportHistoryRepository;
import com.winbaoxian.testng.platform.service.helper.DataWrapHelper;
import com.winbaoxian.testng.platform.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DashboardService {

    @Resource
    private PlatformDashboardRepository platformDashboardRepository;
    @Resource
    private PlatformProjectQualityReportHistoryRepository platformProjectQualityReportHistoryRepository;
    @Resource
    private DataWrapHelper dataWrapHelper;

    @Cacheable(cacheNames = WinTestNGPlatformConstant.DASHBOARD_CACHE_KEY_PREFIX, key = "#root.methodName")
    public DashboardChartDTO getProjectIntegrationRateChart() {
        List<DashboardChartItemDTO> itemList = platformDashboardRepository.getProjectIntegrationItemList();
        completeDashboardChartItemDTO(itemList, Arrays.asList("未集成", "已集成"));
        DashboardChartDTO chartDTO = new DashboardChartDTO();
        chartDTO.setTitle("项目集成率");
        chartDTO.setValues(itemList);
        return chartDTO;
    }

    @Cacheable(cacheNames = WinTestNGPlatformConstant.DASHBOARD_CACHE_KEY_PREFIX, key = "#root.methodName")
    public DashboardChartDTO getIntegrationProjectPassRateChart() {
        List<DashboardChartItemDTO> itemList = platformDashboardRepository.getIntegrationProjectPassItemList();
        completeDashboardChartItemDTO(itemList, Arrays.asList("未通过", "通过"));
        DashboardChartDTO chartDTO = new DashboardChartDTO();
        chartDTO.setTitle("已集成项目上线通过率");
        chartDTO.setValues(itemList);
        return chartDTO;
    }

    @Cacheable(cacheNames = WinTestNGPlatformConstant.DASHBOARD_CACHE_KEY_PREFIX, key = "#root.methodName")
    public DashboardProjectSummaryDTO getProjectSummaryReport() {
        DashboardProjectSummaryDTO summaryDTO = platformDashboardRepository.getProjectSummaryReport();
        summaryDTO.setProjectIntegrationRate(summaryDTO.getProjectCount() == 0 ? 1.0 : summaryDTO.getProjectIntegrationCount() * 1.0 / summaryDTO.getProjectCount());
        summaryDTO.setProjectIntegrationPassRate(summaryDTO.getProjectIntegrationCount() == 0 ? 1.0 : summaryDTO.getProjectIntegrationPassCount() * 1.0 / summaryDTO.getProjectIntegrationCount());
        summaryDTO.setIntegrationOfflineTestCasesCount(summaryDTO.getIntegrationTestCasesCount() - summaryDTO.getIntegrationOnlineTestCasesCount());
        summaryDTO.setIntegrationOnlineTestCasesPassRate(summaryDTO.getIntegrationOnlineTestCasesCount() == 0 ? 1.0 : summaryDTO.getIntegrationOnlineTestCasesPassCount() * 1.0 / summaryDTO.getIntegrationOnlineTestCasesCount());
        return summaryDTO;
    }

    @Cacheable(cacheNames = WinTestNGPlatformConstant.DASHBOARD_CACHE_KEY_PREFIX, key = "#root.methodName+'_'+#pagination.pageNum+'_'+#pagination.pageSize")
    public PaginationDTO<DashboardProjectDetailDTO> getProjectDetailPage(Pagination pagination) {
        int count = platformDashboardRepository.getProjectTotalCount();
        List<DashboardProjectDetailDTO> list = new ArrayList<>();
        if (count > 0) {
            list = platformDashboardRepository.getProjectDetailList(pagination);
        }
        if (CollectionUtils.isNotEmpty(list)) {
            list.stream().forEach(o -> {
                o.setTestCasesFailCount(o.getTestCasesCount() - o.getTestCasesPassCount());
                o.setTestCasesPassRate(o.getTestCasesCount() == 0 ? 1.0 : o.getTestCasesPassCount() * 1.0 / o.getTestCasesCount());
                o.setOfflineTestCasesCount(o.getTestCasesCount() - o.getOnlineTestCasesCount());
                o.setOnlineTestCasesFailCount(o.getOnlineTestCasesCount() - o.getOnlineTestCasesPassCount());
                o.setOnlineTestCasesPassRate(o.getOnlineTestCasesCount() == 0 ? 1.0 : o.getOnlineTestCasesPassCount() * 1.0 / o.getOnlineTestCasesCount());
            });
        }
        return PaginationDTO.createNewInstance(pagination, count, list);
    }

    @Cacheable(cacheNames = WinTestNGPlatformConstant.DASHBOARD_CACHE_KEY_PREFIX, key = "#root.methodName +'_'+ T(org.apache.commons.codec.digest.DigestUtils).md5Hex(T(com.alibaba.fastjson.JSON).toJSONString(#params))")
    public DashboardChartDTO getTestCasesPassRateChart(DashboardTestCasesParams params) {
        commonInitDashboardTestCasesParams(params);
        List<DashboardChartItemDTO> itemList = platformDashboardRepository.getTestCasesPassItemList(params);
        completeDashboardChartItemDTO(itemList, Arrays.asList("失败数", "成功数"));
        DashboardChartDTO chartDTO = new DashboardChartDTO();
        chartDTO.setTitle("测试任务当前通过率");
        chartDTO.setValues(itemList);
        return chartDTO;
    }

    @Cacheable(cacheNames = WinTestNGPlatformConstant.DASHBOARD_CACHE_KEY_PREFIX, key = "#root.methodName +'_'+ T(org.apache.commons.codec.digest.DigestUtils).md5Hex(T(com.alibaba.fastjson.JSON).toJSONString(#params))")
    public DashboardChartDTO getTestCasesRunPassRateChart(DashboardTestCasesParams params) {
        commonInitDashboardTestCasesParams(params);
        List<DashboardChartItemDTO> itemList = platformDashboardRepository.getTestCasesRunPassItemList(params);
        completeDashboardChartItemDTO(itemList, Arrays.asList("未通过数", "通过数"));
        DashboardChartDTO chartDTO = new DashboardChartDTO();
        chartDTO.setTitle("运行通过率");
        chartDTO.setValues(itemList);
        return chartDTO;
    }

    @Cacheable(cacheNames = WinTestNGPlatformConstant.DASHBOARD_CACHE_KEY_PREFIX, key = "#root.methodName +'_'+ T(org.apache.commons.codec.digest.DigestUtils).md5Hex(T(com.alibaba.fastjson.JSON).toJSONString(#params))")
    public DashboardTestCasesSummaryDTO getTestCasesSummaryReport(DashboardTestCasesParams params) {
        commonInitDashboardTestCasesParams(params);
        DashboardTestCasesSummaryDTO summaryDTO = platformDashboardRepository.getTestCasesSummaryReport(params);
        summaryDTO.setTestCasesPassRate(summaryDTO.getTestCasesCount() == 0 ? 1.0 : summaryDTO.getTestCasesLastPassCount() * 1.0 / summaryDTO.getTestCasesCount());
        summaryDTO.setTestCasesRunPassRate(summaryDTO.getTestCasesRunCount() == 0 ? 1.0 : summaryDTO.getTestCasesRunPassCount() * 1.0 / summaryDTO.getTestCasesRunCount());
        return summaryDTO;
    }

    @Cacheable(cacheNames = WinTestNGPlatformConstant.DASHBOARD_CACHE_KEY_PREFIX, key = "#root.methodName +'_'+ T(org.apache.commons.codec.digest.DigestUtils).md5Hex(T(com.alibaba.fastjson.JSON).toJSONString(#params))+'_'+#pagination.pageNum+'_'+#pagination.pageSize")
    public PaginationDTO<DashboardTestCasesDetailDTO> getTestCasesDetailPage(DashboardTestCasesParams params, Pagination pagination) {
        commonInitDashboardTestCasesParams(params);
        int count = platformDashboardRepository.getTestCasesTotalCount(params);
        List<DashboardTestCasesDetailDTO> list = new ArrayList<>();
        if (count > 0) {
            list = platformDashboardRepository.getTestCasesDetailList(params, pagination);
        }
        if (CollectionUtils.isNotEmpty(list)) {
            list.stream().forEach(o -> o.setRunPassRate(o.getRunCount() == 0 ? 1.0 : o.getRunPassCount() * 1.0 / o.getRunCount()));
        }
        return PaginationDTO.createNewInstance(pagination, count, list);
    }

    private void commonInitDashboardTestCasesParams(DashboardTestCasesParams params) {
        if (params == null) {
            params = new DashboardTestCasesParams();
        }
        if (params.getEndTime() == null) {
            params.setEndTime(DateTimeUtils.INSTANCE.getToday().getTime());
        } else {
            params.setEndTime(DateTimeUtils.INSTANCE.getStrictEndTime(params.getEndTime()));
        }
        if (params.getStartTime() == null) {
            params.setStartTime(DateTimeUtils.INSTANCE.getDatetime(new Date(params.getEndTime()), -6).getTime());
        }
    }

    private void completeDashboardChartItemDTO(List<DashboardChartItemDTO> itemList, List<String> fullTitles) {
        if (CollectionUtils.isEmpty(fullTitles)) {
            return;
        }
        if (CollectionUtils.size(itemList) < fullTitles.size()) {
            List<String> titleList = itemList.stream().map(o -> o.getTitle()).collect(Collectors.toList());
            String[] titles = titleList.toArray(new String[]{});
            for (String title : fullTitles) {
                if (!StringUtils.equalsAny(title, titles)) {
                    DashboardChartItemDTO item = new DashboardChartItemDTO();
                    item.setTitle(title);
                    item.setValue(0L);
                    itemList.add(item);
                }
            }
            itemList.sort(Comparator.comparing(o -> fullTitles.indexOf(o.getTitle())));
        }
    }


    @Cacheable(cacheNames = WinTestNGPlatformConstant.DASHBOARD_CACHE_KEY_PREFIX, key = "#root.methodName +'_'+ T(org.apache.commons.codec.digest.DigestUtils).md5Hex(T(com.alibaba.fastjson.JSON).toJSONString(#params))")
    public List<PlatformProjectQualityReportHistoryDTO> getProjectQualityReportForDashboard(DashboardProjectQualityReportParams params) {
        return getProjectQualityReport(params, true);
    }

    public List<PlatformProjectQualityReportHistoryDTO> getProjectQualityReport(DashboardProjectQualityReportParams params, boolean withScoreSwitch) {
        if (params == null) {
            params = new DashboardProjectQualityReportParams();
        }
        if (params.getEndTime() == null) {
            params.setEndTime(DateTimeUtils.INSTANCE.getToday().getTime());
        } else {
            params.setEndTime(DateTimeUtils.INSTANCE.getStrictEndTime(params.getEndTime()));
        }
        if (params.getStartTime() == null) {
            params.setStartTime(DateTimeUtils.INSTANCE.getDatetime(new Date(params.getEndTime()), -6).getTime());
        }
        //项目信息
        List<PlatformProjectQualityReportHistoryDTO> projectList = platformDashboardRepository.getProjectInfoListForQualityReport();
        //项目执行信息
        List<PlatformProjectTestReportDTO> runDataList = platformDashboardRepository.getProjectTestReportListForQualityReport(params);
        Map<Long, List<PlatformProjectTestReportDTO>> runDataMap = runDataList.stream().collect(Collectors.groupingBy(o -> o.getProjectId()));
        List<PlatformProjectQualityReportHistoryDTO> retList = new ArrayList<>();
        for (PlatformProjectQualityReportHistoryDTO dto : projectList) {
            Long projectId = dto.getProjectId();
            List<PlatformProjectTestReportDTO> runList = runDataMap.get(projectId);
            dataWrapHelper.wrapProjectQualityReportWithRunInfo(dto, runList);
            retList.add(dto);
        }
        dataWrapHelper.wrapPlatformProjectQualityReportHistoryList(retList, withScoreSwitch);
        return retList;
    }

    public List<String> getSelectDateRangeListForProjectQualityReportHistory() {
        return platformDashboardRepository.getSelectDateRangeListForProjectQualityReportHistory();
    }

    public List<PlatformProjectQualityReportHistoryDTO> getProjectQualityReportHistory(String dateRange) {
        if (StringUtils.isBlank(dateRange)) {
            return null;
        }
        List<ProjectQualityReportHistoryEntity> entityList = platformProjectQualityReportHistoryRepository.findByDateRangeAndDeletedFalse(dateRange);
        List<PlatformProjectQualityReportHistoryDTO> dtoList = PlatformProjectQualityReportHistoryMapper.INSTANCE.toDTOList(entityList);
        dataWrapHelper.wrapPlatformProjectQualityReportHistoryList(dtoList);
        return dtoList;
    }

}
