package com.winbaoxian.testng.platform.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.winbaoxian.testng.core.common.ParamsExecutor;
import com.winbaoxian.testng.model.dto.ApiDTO;
import com.winbaoxian.testng.model.entity.ApiEntity;
import com.winbaoxian.testng.model.entity.ProjectQualityReportHistoryEntity;
import com.winbaoxian.testng.platform.model.constant.WinTestNGPlatformConstant;
import com.winbaoxian.testng.platform.model.dto.DashboardProjectQualityReportParams;
import com.winbaoxian.testng.platform.model.dto.PlatformProjectDTO;
import com.winbaoxian.testng.platform.model.dto.PlatformProjectQualityReportHistoryDTO;
import com.winbaoxian.testng.platform.model.enums.PlatformSysSettingsKey;
import com.winbaoxian.testng.platform.model.mapper.PlatformApiMapper;
import com.winbaoxian.testng.platform.model.mapper.PlatformProjectQualityReportHistoryMapper;
import com.winbaoxian.testng.platform.repository.PlatformApiRepository;
import com.winbaoxian.testng.platform.repository.PlatformProjectQualityReportHistoryRepository;
import com.winbaoxian.testng.platform.service.helper.PlatformTestNGHelper;
import com.winbaoxian.testng.platform.utils.DateTimeUtils;
import com.winbaoxian.testng.platform.utils.FileReaderUtils;
import com.winbaoxian.testng.service.EmailSenderService;
import com.winbaoxian.testng.service.WinTestNGService;
import com.winbaoxian.testng.utils.DateFormatUtils;
import com.winbaoxian.testng.utils.HttpUtils;
import com.winbaoxian.testng.utils.UrlParserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class SchedulerService {

    @Resource
    private ProjectService projectService;
    @Resource
    private PlatformTestNGHelper platformTestNGHelper;
    @Resource
    private DashboardService dashboardService;
    @Resource
    private PlatformProjectQualityReportHistoryRepository platformProjectQualityReportHistoryRepository;
    @Resource
    private ParamsExecutor paramsExecutor;
    @Resource
    private EmailSenderService emailSenderService;
    @Resource
    private WinTestNGService winTestNGService;
    @Resource
    private PlatformApiRepository platformApiRepository;

    @Value("${project.domain}")
    private String projectDomain;

    public void integrationProjectRunForAutomation() {
        List<PlatformProjectDTO> projectDTOList = projectService.getByIntegrationFlagTrue();
        log.info("integrationProjectRunForAutomation, project count:{}", CollectionUtils.size(projectDTOList));
        if (CollectionUtils.isEmpty(projectDTOList)) {
            return;
        }
        for (PlatformProjectDTO projectDTO : projectDTOList) {
            log.info("integrationProjectRunForAutomation, project[id:{}, name:{}] start run testCases", projectDTO.getId(), projectDTO.getName());
            try {
                platformTestNGHelper.executeOnlineTestCasesByProjectId(projectDTO.getId());
            } catch (Exception e) {
                log.error("integrationProjectRunForAutomation, project[id:{}, name:{}] run error, msg:{}", projectDTO.getId(), projectDTO.getName(), e.getMessage(), e);
            }
        }
    }

    @Transactional
    public void autoStoreProjectQualityReportHistory() {
        Date yesterday = DateTimeUtils.INSTANCE.getYesterday();
        Date startDay = DateTimeUtils.INSTANCE.getDatetime(yesterday, -6);
        String dateRange = String.format("%s~%s", DateFormatUtils.INSTANCE.ymdFormat(startDay), DateFormatUtils.INSTANCE.ymdFormat(yesterday));
        if (platformProjectQualityReportHistoryRepository.existsByDateRangeAndDeletedFalse(dateRange)) {
            log.info("autoStoreProjectQualityReportHistory, 数据已存在，跳过, dateRange: {}", dateRange);
            return;
        }
        DashboardProjectQualityReportParams params = new DashboardProjectQualityReportParams();
        params.setStartTime(startDay.getTime());
        params.setEndTime(yesterday.getTime());
        List<PlatformProjectQualityReportHistoryDTO> historyList = dashboardService.getProjectQualityReport(params, false);
        if (CollectionUtils.isEmpty(historyList)) {
            return;
        }
        for (PlatformProjectQualityReportHistoryDTO history : historyList) {
            history.setId(null);
            history.setStartTime(startDay);
            history.setEndTime(yesterday);
            history.setDateRange(dateRange);
            if (CollectionUtils.isNotEmpty(history.getFailInfoList())) {
                history.setRunFailInfo(JSON.toJSONString(history.getFailInfoList()));
            }
        }
        List<ProjectQualityReportHistoryEntity> entityList = PlatformProjectQualityReportHistoryMapper.INSTANCE.toEntityList(historyList);
        platformProjectQualityReportHistoryRepository.save(entityList);
        log.info("autoStoreProjectQualityReportHistory, 保存数据成功, dateRange: {}, count: {}", dateRange, entityList.size());
    }

    public void sendProjectQualityReportWeekly() {
        Date yesterday = DateTimeUtils.INSTANCE.getYesterday();
        Date startDay = DateTimeUtils.INSTANCE.getDatetime(yesterday, -6);
        String dateRange = String.format("%s~%s", DateFormatUtils.INSTANCE.ymdFormat(startDay), DateFormatUtils.INSTANCE.ymdFormat(yesterday));
        if (!platformProjectQualityReportHistoryRepository.existsByDateRangeAndDeletedFalse(dateRange)) {
            log.info("sendProjectQualityReportWeekly, 数据不存在, dateRange: {}", dateRange);
            return;
        }
        String emailTemplate = FileReaderUtils.INSTANCE.getResourceFileContent("templates/email/projectQualityWeekReport.ftl");
        if (StringUtils.isBlank(emailTemplate)) {
            log.info("sendProjectQualityReportWeekly, 邮件模板不存在, dateRange: {}", dateRange);
            return;
        }
        List<PlatformProjectQualityReportHistoryDTO> dtoList = dashboardService.getProjectQualityReportHistory(dateRange);
        if (CollectionUtils.isEmpty(dtoList)) {
            log.info("sendProjectQualityReportWeekly, 未找到报告数据, dateRange: {}", dateRange);
            return;
        }
        Map<String, Object> model = new HashMap<>();
        model.put("dateRange", dateRange);
        model.put("list", dtoList);
        model.put("projectDomain", projectDomain);
        String content = null;
        try {
            content = paramsExecutor.render(emailTemplate, model);
        } catch (Exception e) {
            log.error("sendProjectQualityReportWeekly, paramsExecutor.render error, dateRange: {}", dateRange, e);
        }
        //加上url地址
        String reportUrl = String.format("%s/view/report/projectQualityReport/%s", projectDomain, dateRange);
        String reportUrlContent = String.format("<div class=\"container-fluid small\" style=\"margin-bottom:3em\"><h2 class=\"sub-header\">报告地址</h2><div><a href=\"%s\">%s</a></div></div>", reportUrl, reportUrl);
        if (StringUtils.isBlank(content)) {
            content = String.format("<html><body>%s</body></html>", reportUrlContent);
        } else {
            try {
                Document document = Jsoup.parse(content);
                document.body().getElementsByClass("container-fluid").first().before(reportUrlContent);
                content = document.html();
            } catch (Exception e) {
                log.error("sendProjectQualityReportWeekly, add reportUrlContent error, reportUrl :{}", reportUrl);
            }
        }
        String subject = String.format("接口自动化周报%s", dateRange);
        //发送邮件
        String emailStr = winTestNGService.getSysValueByKey(PlatformSysSettingsKey.qualityReportEmailTos.getKeyName());
        if (StringUtils.isBlank(emailStr)) {
            log.info("sendProjectQualityReportWeekly, 邮件未配置, dateRange: {}", dateRange);
            return;
        }
        String[] emails = StringUtils.split(emailStr.trim(), WinTestNGPlatformConstant.CHAR_COMMA);
        emailSenderService.sendMailSync(subject, content, Arrays.asList(emails));
    }

    /**
     * 通过分析项目的apidoc文档，获取api接口数据
     */
    @Transactional
    public void analysisProjectApiInfo() {
        List<PlatformProjectDTO> projectDTOList = projectService.getByIntegrationFlagTrue();
        if (CollectionUtils.isEmpty(projectDTOList)) {
            return;
        }
        for (PlatformProjectDTO projectDTO : projectDTOList) {
            if (StringUtils.isBlank(projectDTO.getGitAddress())) {
                continue;
            }
            if (BooleanUtils.isNotTrue(projectDTO.getSyncApiDataFlag())) {
                continue;
            }
            log.info("analysisProjectApiInfo sync start, projectName:{}", projectDTO.getName());
            try {
                List<ApiDTO> apiDTOList = null;
                if (projectDTO.getId().equals(30L)) {
                    //原生项目特殊处理
                    String dataUrl = String.format(WinTestNGPlatformConstant.SERVICEDOC_URL_PATTERN, "wy_rpc_protocol");
                    String data = HttpUtils.INSTANCE.doGet(dataUrl);
                    apiDTOList = parseServiceDataJson(data, projectDTO.getId());
                } else {
                    String projectName = StringUtils.removeEnd(StringUtils.substringAfterLast(projectDTO.getGitAddress(), WinTestNGPlatformConstant.CHAR_SEPARATOR), WinTestNGPlatformConstant.GIT_SUFFIX);
                    String dataUrl = String.format(WinTestNGPlatformConstant.APIDOC_URL_PATTERN, projectName);
                    String data = HttpUtils.INSTANCE.doGet(dataUrl);
                    apiDTOList = parseApiDataJson(data, projectDTO.getId());
                }
                if (CollectionUtils.isNotEmpty(apiDTOList)) {
                    List<ApiEntity> apiEntityList = PlatformApiMapper.INSTANCE.toEntityList(apiDTOList);
                    platformApiRepository.save(apiEntityList);
                    log.info("analysisProjectApiInfo sync end, projectName:{},  count:{}", projectDTO.getName(), apiDTOList.size());
                } else {
                    log.info("analysisProjectApiInfo sync end, projectName:{},  no data ", projectDTO.getName());
                }
            } catch (Exception e) {
                log.error("analysisProjectApiInfo failed, projectName:{}", projectDTO.getName(), e);
            }
        }

    }


    private static final Pattern METHOD_NAME_PATTERN = Pattern.compile("[^\\s]+\\s+(\\w+)\\([^\\)]+\\)");

    private List<ApiDTO> parseServiceDataJson(String data, Long productId) {
        if (StringUtils.isBlank(data)) {
            return null;
        }
        JSONArray jsonArray = JSON.parseArray(data);
        if (CollectionUtils.isEmpty(jsonArray)) {
            return null;
        }
        List<ApiDTO> apiDTOList = new ArrayList<>();
        Set<String> urlSet = new HashSet<>();
        for (Object o : jsonArray) {
            JSONObject jo = (JSONObject) o;
            String group = StringUtils.trim(jo.getString("group"));
            String service = StringUtils.trim(jo.getString("service"));
            if (StringUtils.isBlank(group) || group.length() < 2 || StringUtils.isBlank(service)) {
                continue;
            }
            Matcher matcher = METHOD_NAME_PATTERN.matcher(service);
            if (!matcher.find()) {
                continue;
            }
            String methodName = matcher.group(1);
            String groupName = group;
            if (StringUtils.isAllUpperCase(group.substring(0, 2))) {
                groupName = StringUtils.substring(groupName, 1);
            }
            groupName = StringUtils.removeEndIgnoreCase(groupName, "Service");
            groupName = StringUtils.uncapitalize(groupName);
            String url = String.format("%s/1/%s", groupName, methodName);
            String apiUrl = UrlParserUtils.INSTANCE.parseStrictRequestPath(url);
            if (urlSet.contains(apiUrl)) {
                continue;
            }
            urlSet.add(apiUrl);
            if (platformApiRepository.existsByProjectIdAndCheckKey(productId, apiUrl)) {
                continue;
            }
            ApiDTO apiDTO = new ApiDTO();
            apiDTO.setApiTitle(jo.getString("name"));
            apiDTO.setApiUrl(apiUrl);
            apiDTO.setCheckKey(apiUrl);
            apiDTO.setProjectId(productId);
            apiDTO.setTargetFlag(false);
            apiDTO.setFinishFlag(false);
            apiDTOList.add(apiDTO);
        }
        return apiDTOList;
    }


    private List<ApiDTO> parseApiDataJson(String data, Long productId) {
        if (StringUtils.isBlank(data)) {
            return null;
        }
        JSONArray jsonArray = JSON.parseArray(data);
        if (CollectionUtils.isEmpty(jsonArray)) {
            return null;
        }
        List<ApiDTO> apiDTOList = new ArrayList<>();
        Set<String> urlSet = new HashSet<>();
        for (Object o : jsonArray) {
            JSONObject jo = (JSONObject) o;
            String url = jo.getString("url");
            if (StringUtils.isBlank(url)) {
                continue;
            }
            String apiUrl = UrlParserUtils.INSTANCE.parseStrictRequestPath(url);
            if (urlSet.contains(apiUrl)) {
                continue;
            }
            urlSet.add(apiUrl);
            if (platformApiRepository.existsByProjectIdAndCheckKey(productId, apiUrl)) {
                continue;
            }
            ApiDTO apiDTO = new ApiDTO();
            apiDTO.setApiTitle(jo.getString("title"));
            apiDTO.setApiUrl(apiUrl);
            apiDTO.setCheckKey(apiUrl);
            apiDTO.setProjectId(productId);
            apiDTO.setTargetFlag(false);
            apiDTO.setFinishFlag(false);
            apiDTOList.add(apiDTO);
        }
        return apiDTOList;
    }

}