package com.winbaoxian.testng.platform.controller;

import com.winbaoxian.testng.platform.exception.WinTestNgPlatformException;
import com.winbaoxian.testng.platform.model.dto.PlatformProjectQualityReportHistoryDTO;
import com.winbaoxian.testng.platform.model.dto.PlatformTestReportDTO;
import com.winbaoxian.testng.platform.model.dto.PlatformTestReportDetailsDTO;
import com.winbaoxian.testng.platform.service.DashboardService;
import com.winbaoxian.testng.platform.service.ReportService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2019-03-28 19:57
 */
@Controller
@RequestMapping(value = "view/report")
public class ReportViewController {

    @Resource
    private ReportService reportService;
    @Resource
    private DashboardService dashboardService;
    @Value("${project.domain}")
    private String projectDomain;

    @RequestMapping(value = "summary/{reportUuid}")
    public String testReportSummary(@PathVariable("reportUuid") String reportUuid, Map<String, Object> model) {
        PlatformTestReportDTO report = reportService.getTestReport(reportUuid);
        List<PlatformTestReportDetailsDTO> list = reportService.getTestReportDetailsList(reportUuid);
        if (report == null || CollectionUtils.isEmpty(list)) {
            throw new WinTestNgPlatformException("数据异常");
        }
        model.put("report", report);
        model.put("list", list);
        model.put("projectDomain", projectDomain);
        return "reportSummary";
    }


    @RequestMapping(value = "details/{reportUuid}/{testCasesId}")
    public String testReportDetails(@PathVariable("reportUuid") String reportUuid, @PathVariable("testCasesId") Long testCasesId, Map<String, Object> model) {
        PlatformTestReportDetailsDTO reportDetails = reportService.getTestReportDetails(reportUuid, testCasesId);
        if (reportDetails == null) {
            throw new WinTestNgPlatformException("数据异常");
        }
        model.put("detail", reportDetails);
        model.put("projectDomain", projectDomain);
        return "reportDetail";
    }


    @RequestMapping(value = "projectQualityReport/{dateRange}")
    public String projectQualityReport(@PathVariable("dateRange") String dateRange, Map<String, Object> model) {
        List<PlatformProjectQualityReportHistoryDTO> dtoList = dashboardService.getProjectQualityReportHistory(dateRange);
        if (CollectionUtils.isEmpty(dtoList)) {
            throw new WinTestNgPlatformException("数据异常");
        }
        model.put("dateRange", dateRange);
        model.put("list", dtoList);
        model.put("projectDomain", projectDomain);
        return "email/projectQualityWeekReport";
    }

}
