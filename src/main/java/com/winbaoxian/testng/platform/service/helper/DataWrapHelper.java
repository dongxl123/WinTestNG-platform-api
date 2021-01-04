package com.winbaoxian.testng.platform.service.helper;

import com.alibaba.fastjson.JSON;
import com.winbaoxian.module.security.model.dto.WinSecurityBaseUserDTO;
import com.winbaoxian.testng.enums.ReportFailReason;
import com.winbaoxian.testng.enums.RunState;
import com.winbaoxian.testng.platform.model.constant.WinTestNGPlatformConstant;
import com.winbaoxian.testng.platform.model.dto.*;
import com.winbaoxian.testng.platform.model.enums.PlatformSysSettingsKey;
import com.winbaoxian.testng.platform.service.*;
import com.winbaoxian.testng.service.WinTestNGService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dongxuanliang252
 * @date 2019-03-25 14:01
 */
@Service
@Slf4j
public class DataWrapHelper {

    @Resource
    private UserService userService;
    @Resource
    private ProjectService projectService;
    @Resource
    private ModuleService moduleService;
    @Resource
    private TestCasesService testCasesService;
    @Resource
    private ReportService reportService;
    @Resource
    private WinTestNGService winTestNGService;
    @Value("${project.domain}")
    private String projectDomain;

    public void wrapActionTemplateList(List<PlatformActionTemplateDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return;
        }
        List<Long> userIdList = dtoList.stream().filter(o -> o.getCreatorUid() != null).map(o -> o.getCreatorUid()).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        List<WinSecurityBaseUserDTO> userDTOList = userService.getByIdList(userIdList);
        if (CollectionUtils.isEmpty(userDTOList)) {
            return;
        }
        Map<Long, String> idNameMap = userDTOList.stream().collect(Collectors.toMap(o -> o.getId(), o -> o.getName()));
        for (PlatformActionTemplateDTO templateDTO : dtoList) {
            if (templateDTO.getCreatorUid() != null && idNameMap.containsKey(templateDTO.getCreatorUid())) {
                templateDTO.setCreatorName(idNameMap.get(templateDTO.getCreatorUid()));
            }
        }
    }

    public void wrapTestCasesList(List<PlatformTestCasesDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return;
        }
        Set<Long> userIdSet = new HashSet<>();
        Set<Long> projectIdSet = new HashSet<>();
        Set<Long> moduleIdSet = new HashSet<>();
        String qualityScoreShow = winTestNGService.getSysValueByKey(PlatformSysSettingsKey.qualityScoreTestCasesShow.getKeyName());
        for (PlatformTestCasesDTO dto : dtoList) {
            if (StringUtils.equals(qualityScoreShow, "0")) {
                dto.setQualityScore(null);
            }
            if (dto.getCreatorUid() != null) {
                userIdSet.add(dto.getCreatorUid());
            }
            if (dto.getOwnerUid() != null) {
                userIdSet.add(dto.getOwnerUid());
            }
            if (dto.getProjectId() != null) {
                projectIdSet.add(dto.getProjectId());
            }
            if (dto.getModuleId() != null) {
                moduleIdSet.add(dto.getModuleId());
            }
        }
        Map<Long, String> idNameUserMap = null;
        Map<Long, String> idNameProjectMap = null;
        Map<Long, String> idNameModuleMap = null;
        if (CollectionUtils.isNotEmpty(userIdSet)) {
            List<WinSecurityBaseUserDTO> userDTOList = userService.getByIdList(new ArrayList<>(userIdSet));
            if (CollectionUtils.isNotEmpty(userDTOList)) {
                idNameUserMap = userDTOList.stream().collect(Collectors.toMap(o -> o.getId(), o -> o.getName()));
            }
        }
        if (CollectionUtils.isNotEmpty(projectIdSet)) {
            List<PlatformProjectDTO> projectDTOList = projectService.getByIdList(new ArrayList<>(projectIdSet));
            if (CollectionUtils.isNotEmpty(projectDTOList)) {
                idNameProjectMap = projectDTOList.stream().collect(Collectors.toMap(o -> o.getId(), o -> o.getName()));
            }
        }
        if (CollectionUtils.isNotEmpty(userIdSet)) {
            List<PlatformModuleDTO> moduleDTOList = moduleService.getByIdList(new ArrayList<>(moduleIdSet));
            if (CollectionUtils.isNotEmpty(moduleDTOList)) {
                idNameModuleMap = moduleDTOList.stream().collect(Collectors.toMap(o -> o.getId(), o -> o.getName()));
            }
        }
        for (PlatformTestCasesDTO testCasesDTO : dtoList) {
            if (MapUtils.isNotEmpty(idNameUserMap)) {
                if (testCasesDTO.getCreatorUid() != null && idNameUserMap.containsKey(testCasesDTO.getCreatorUid())) {
                    testCasesDTO.setCreatorName(idNameUserMap.get(testCasesDTO.getCreatorUid()));
                }
                if (testCasesDTO.getOwnerUid() != null && idNameUserMap.containsKey(testCasesDTO.getOwnerUid())) {
                    testCasesDTO.setOwnerName(idNameUserMap.get(testCasesDTO.getOwnerUid()));
                }
            }
            if (MapUtils.isNotEmpty(idNameProjectMap)) {
                if (testCasesDTO.getProjectId() != null && idNameProjectMap.containsKey(testCasesDTO.getProjectId())) {
                    testCasesDTO.setProjectName(idNameProjectMap.get(testCasesDTO.getProjectId()));
                }
            }
            if (MapUtils.isNotEmpty(idNameModuleMap)) {
                if (testCasesDTO.getModuleId() != null && idNameModuleMap.containsKey(testCasesDTO.getModuleId())) {
                    testCasesDTO.setModuleName(idNameModuleMap.get(testCasesDTO.getModuleId()));
                }
            }
        }
    }

    public void wrapTestReportList(List<PlatformTestReportDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return;
        }
        //用户
        List<Long> userIdList = dtoList.stream().filter(o -> o.getExecutorUid() != null).map(o -> o.getExecutorUid()).distinct().collect(Collectors.toList());
        Map<Long, String> userIdNameMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(userIdList)) {
            List<WinSecurityBaseUserDTO> userDTOList = userService.getByIdList(userIdList);
            if (CollectionUtils.isNotEmpty(userDTOList)) {
                userIdNameMap = userDTOList.stream().collect(Collectors.toMap(o -> o.getId(), o -> o.getName()));
            }
        }
        //项目名称
        List<String> reportUuidList = dtoList.stream().map(o -> o.getReportUuid()).collect(Collectors.toList());
        Map<String, List<PlatformTestReportDetailsDTO>> reportUuidDetailsMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(reportUuidList)) {
            List<PlatformTestReportDetailsDTO> testCasesDTOList = reportService.getSimpleTestReportDetailsList(reportUuidList);
            reportUuidDetailsMap = testCasesDTOList.stream().collect(Collectors.groupingBy(o -> o.getReportUuid()));
        }
        for (PlatformTestReportDTO reportDTO : dtoList) {
            //用户名称
            if (MapUtils.isNotEmpty(userIdNameMap) && reportDTO.getExecutorUid() != null && userIdNameMap.containsKey(reportDTO.getExecutorUid())) {
                reportDTO.setExecutorName(userIdNameMap.get(reportDTO.getExecutorUid()));
            }
            //项目名称、测试任务名称
            if (MapUtils.isNotEmpty(reportUuidDetailsMap) && reportDTO.getReportUuid() != null && reportUuidDetailsMap.containsKey(reportDTO.getReportUuid())) {
                List<PlatformTestReportDetailsDTO> testReportDetailsDTOS = reportUuidDetailsMap.get(reportDTO.getReportUuid());
                if (CollectionUtils.isNotEmpty(testReportDetailsDTOS)) {
                    List<String> projectNameList = testReportDetailsDTOS.stream().map(o -> o.getProjectName()).distinct().collect(Collectors.toList());
                    List<String> testCasesNameList = testReportDetailsDTOS.stream().map(o -> o.getTestCasesName()).distinct().collect(Collectors.toList());
                    if (projectNameList.size() == 1) {
                        reportDTO.setProjectNames(projectNameList.get(0));
                    } else {
                        reportDTO.setProjectNames(String.format("%s...(%s)", projectNameList.get(0), projectNameList.size()));
                    }
                    if (testCasesNameList.size() == 1) {
                        reportDTO.setTestCasesNames(testCasesNameList.get(0));
                    } else {
                        reportDTO.setTestCasesNames(String.format("%s...(%s)", testCasesNameList.get(0), testCasesNameList.size()));
                    }
                }
            }
            //测试报告地址
            reportDTO.setReportUrl(projectDomain + "/view/report/summary/" + reportDTO.getReportUuid());
        }
    }

    public void wrapTestReportDetailsList(List<PlatformTestReportDetailsDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return;
        }
        List<Long> idList = dtoList.stream().filter(o -> o.getTestCasesId() != null).map(o -> o.getTestCasesId()).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        List<PlatformTestCasesDTO> testCasesDTOList = testCasesService.getByIdList(idList);
        if (CollectionUtils.isEmpty(testCasesDTOList)) {
            return;
        }
        wrapTestCasesList(testCasesDTOList);
        Map<Long, PlatformTestCasesDTO> idNameMap = testCasesDTOList.stream().collect(Collectors.toMap(o -> o.getId(), o -> o));
        //用户
        for (PlatformTestReportDetailsDTO detailsDTO : dtoList) {
            if (detailsDTO.getTestCasesId() != null && idNameMap.containsKey(detailsDTO.getTestCasesId())) {
                PlatformTestCasesDTO testCasesDTO = idNameMap.get(detailsDTO.getTestCasesId());
                detailsDTO.setTestCasesName(testCasesDTO.getName());
                detailsDTO.setModuleName(testCasesDTO.getModuleName());
                detailsDTO.setProjectName(testCasesDTO.getProjectName());
                detailsDTO.setOwnerName(testCasesDTO.getOwnerName());
            }
        }
    }

    public void wrapTestReportDetails(PlatformTestReportDetailsDTO dto) {
        if (dto == null || dto.getTestCasesId() == null) {
            return;
        }
        PlatformTestCasesDTO testCasesDTO = testCasesService.getById(dto.getTestCasesId());
        if (testCasesDTO == null) {
            return;
        }
        if (testCasesDTO.getModuleId() != null) {
            PlatformModuleDTO moduleDTO = moduleService.getById(testCasesDTO.getModuleId());
            if (moduleDTO != null) {
                dto.setModuleName(moduleDTO.getName());
            }
        }
        if (testCasesDTO.getProjectId() != null) {
            PlatformProjectDTO projectDTO = projectService.getById(testCasesDTO.getProjectId());
            if (projectDTO != null) {
                dto.setProjectName(projectDTO.getName());
            }
        }
        dto.setTestCasesName(testCasesDTO.getName());
    }

    public void wrapProjectList(List<PlatformProjectDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        //对应测试/开发数据
        Set<Long> ownerIdSet = new HashSet<>();
        String qualityScoreShow = winTestNGService.getSysValueByKey(PlatformSysSettingsKey.qualityScoreProjectShow.getKeyName());
        for (PlatformProjectDTO o : list) {
            if (StringUtils.equals(qualityScoreShow, "0")) {
                o.setQualityScore(null);
            }
            ownerIdSet.addAll(parseOwnerIdList(o.getTestOwnerIds()));
            ownerIdSet.addAll(parseOwnerIdList(o.getDevOwnerIds()));
        }
        Map<Long, String> idNameUserMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(ownerIdSet)) {
            List<WinSecurityBaseUserDTO> userDTOList = userService.getByIdList(new ArrayList<>(ownerIdSet));
            if (CollectionUtils.isNotEmpty(userDTOList)) {
                idNameUserMap = userDTOList.stream().collect(Collectors.toMap(o -> o.getId(), o -> o.getName()));
            }
        }
        for (PlatformProjectDTO projectDTO : list) {
            //接口完成进度
            Long targetCount = projectDTO.getTargetCount();
            Long finishCount = projectDTO.getFinishCount();
            if (targetCount != null && finishCount != null) {
                if (targetCount > 0) {
                    projectDTO.setFinishRate(finishCount * 1.0 / targetCount);
                }
            }
            //项目对应测试
            projectDTO.setTestOwnerList(getOwnerList(projectDTO.getTestOwnerIds(), idNameUserMap));
            //项目对应开发
            projectDTO.setDevOwnerList(getOwnerList(projectDTO.getDevOwnerIds(), idNameUserMap));
        }
    }

    private List<Long> parseOwnerIdList(String ownerIds) {
        try {
            List<Long> ids = JSON.parseArray(WinTestNGPlatformConstant.OPEN_BRACKET + ownerIds + WinTestNGPlatformConstant.CLOSE_BRACKET, Long.class);
            if (CollectionUtils.isNotEmpty(ids)) {
                return ids;
            }
        } catch (Exception e) {
            log.error("parseOwnerIdList数据异常,{}", ownerIds, e);
        }
        return ListUtils.EMPTY_LIST;
    }

    private List<IdNameDTO> getOwnerList(String testOwnerIds, Map<Long, String> idNameUserMap) {
        if (StringUtils.isBlank(testOwnerIds)) {
            return null;
        }
        List<Long> ids = null;
        try {
            ids = JSON.parseArray(WinTestNGPlatformConstant.OPEN_BRACKET + testOwnerIds + WinTestNGPlatformConstant.CLOSE_BRACKET, Long.class);
        } catch (Exception e) {
        }
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        List<IdNameDTO> testOwnerList = new ArrayList<>();
        for (Long id : ids) {
            if (idNameUserMap.containsKey(id)) {
                IdNameDTO idNameDTO = new IdNameDTO();
                idNameDTO.setId(id);
                idNameDTO.setName(idNameUserMap.get(id));
                testOwnerList.add(idNameDTO);
            }
        }
        return testOwnerList;

    }

    public void wrapProjectQualityReportWithRunInfo(PlatformProjectQualityReportHistoryDTO dto, List<PlatformProjectTestReportDTO> runList) {
        if (dto == null) {
            return;
        }
        if (CollectionUtils.isEmpty(runList)) {
            dto.setRunTotalCount(0L);
            dto.setRunSuccessCount(0L);
            return;
        }
        long runFailCount = runList.stream().filter(o -> RunState.FAIL.getValue().equals(o.getRunState())).count();
        dto.setRunTotalCount((long) runList.size());
        dto.setRunSuccessCount(dto.getRunTotalCount() - runFailCount);
        if (runFailCount <= 0) {
            return;
        }
        Map<Integer, List<PlatformProjectTestReportDTO>> failTestReportMap = runList.stream().filter(o -> RunState.FAIL.getValue().equals(o.getRunState()) && o.getFailReasonId() != null).collect(Collectors.groupingBy(o -> o.getFailReasonId()));
        long hasFailReasonCount = failTestReportMap.size();
        List<PlatformProjectQualityReportHistoryDTO.RunFailInfoDTO> failInfoList = new ArrayList<>();
        for (ReportFailReason failReason : ReportFailReason.values()) {
            Integer reasonId = failReason.getValue();
            if (!failTestReportMap.containsKey(reasonId)) {
                continue;
            }
            List<PlatformProjectTestReportDTO> oneErrorReasonTestReportList = failTestReportMap.get(reasonId);
            PlatformProjectQualityReportHistoryDTO.RunFailInfoDTO failInfo = new PlatformProjectQualityReportHistoryDTO.RunFailInfoDTO();
            failInfo.setFailReason(failReason.getTitle());
            failInfo.setNeedFixFlag(failReason.getNeedFixFlag());
            failInfo.setFailNum((long) oneErrorReasonTestReportList.size());
            failInfo.setFixNum(oneErrorReasonTestReportList.stream().filter(o -> BooleanUtils.isTrue(o.getFixFlag())).count());
            failInfoList.add(failInfo);
        }
        if (runFailCount > hasFailReasonCount) {
            PlatformProjectQualityReportHistoryDTO.RunFailInfoDTO failInfo = new PlatformProjectQualityReportHistoryDTO.RunFailInfoDTO();
            failInfo.setFailReason("未知原因");
            failInfo.setNeedFixFlag(true);
            failInfo.setFailNum(runFailCount - hasFailReasonCount);
            failInfo.setFixNum(0L);
            failInfoList.add(failInfo);
        }
        dto.setFailInfoList(failInfoList);
    }

    public void wrapPlatformProjectQualityReportHistoryList(List<PlatformProjectQualityReportHistoryDTO> dtoList) {
        wrapPlatformProjectQualityReportHistoryList(dtoList, true);
    }

    public void wrapPlatformProjectQualityReportHistoryList(List<PlatformProjectQualityReportHistoryDTO> dtoList, boolean withScoreSwitch) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return;
        }
        //对应测试/开发数据
        Set<Long> ownerIdSet = new HashSet<>();
        //项目数据
        Set<Long> projectIdSet = new HashSet<>();
        for (PlatformProjectQualityReportHistoryDTO o : dtoList) {
            ownerIdSet.addAll(parseOwnerIdList(o.getTestOwnerIds()));
            ownerIdSet.addAll(parseOwnerIdList(o.getDevOwnerIds()));
            projectIdSet.add(o.getProjectId());
        }
        Map<Long, String> idNameUserMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(ownerIdSet)) {
            List<WinSecurityBaseUserDTO> userDTOList = userService.getByIdList(new ArrayList<>(ownerIdSet));
            if (CollectionUtils.isNotEmpty(userDTOList)) {
                idNameUserMap = userDTOList.stream().collect(Collectors.toMap(o -> o.getId(), o -> o.getName()));
            }
        }
        //项目名称
        Map<Long, String> idNameProjectMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(projectIdSet)) {
            List<PlatformProjectDTO> projectDTOList = projectService.getByIdList(new ArrayList<>(projectIdSet));
            if (CollectionUtils.isNotEmpty(projectDTOList)) {
                idNameProjectMap = projectDTOList.stream().collect(Collectors.toMap(o -> o.getId(), o -> o.getName()));
            }
        }
        String qualityScoreShow = null;
        if (withScoreSwitch) {
            qualityScoreShow = winTestNGService.getSysValueByKey(PlatformSysSettingsKey.qualityScoreReportProjectShow.getKeyName());
        }
        for (PlatformProjectQualityReportHistoryDTO dto : dtoList) {
            if (StringUtils.equals(qualityScoreShow, "0")) {
                dto.setQualityScore(null);
            }
            if (StringUtils.isNotBlank(dto.getRunFailInfo())) {
                dto.setFailInfoList(JSON.parseArray(dto.getRunFailInfo(), PlatformProjectQualityReportHistoryDTO.RunFailInfoDTO.class));
            }
            dto.setRunFailCount(dto.getRunTotalCount() - dto.getRunSuccessCount());
            if (dto.getApiTargetCount() != null && dto.getApiFinishCount() != null) {
                if (dto.getApiTargetCount() > 0) {
                    dto.setApiFinishRate(dto.getApiFinishCount() * 1.0 / dto.getApiTargetCount());
                }
            }
            dto.setOnlineTestCasesPassRate(dto.getOnlineTestCasesCount() == 0 ? 1.0 : dto.getOnlineTestCasesSuccessCount() * 1.0 / dto.getOnlineTestCasesCount());
            //项目对应测试
            dto.setTestOwnerList(getOwnerList(dto.getTestOwnerIds(), idNameUserMap));
            //项目对应开发
            dto.setDevOwnerList(getOwnerList(dto.getDevOwnerIds(), idNameUserMap));
            //项目名称
            dto.setProjectName(idNameProjectMap.get(dto.getProjectId()));
        }
    }

}
