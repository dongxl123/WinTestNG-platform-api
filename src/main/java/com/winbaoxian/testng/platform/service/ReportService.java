package com.winbaoxian.testng.platform.service;

import com.winbaoxian.testng.model.entity.TestCasesEntity;
import com.winbaoxian.testng.model.entity.TestReportDetailsEntity;
import com.winbaoxian.testng.model.entity.TestReportEntity;
import com.winbaoxian.testng.platform.exception.WinTestNgPlatformException;
import com.winbaoxian.testng.platform.model.common.Pagination;
import com.winbaoxian.testng.platform.model.common.PaginationDTO;
import com.winbaoxian.testng.platform.model.constant.WinTestNGPlatformConstant;
import com.winbaoxian.testng.platform.model.dto.*;
import com.winbaoxian.testng.platform.model.mapper.PlatformTestReportDetailsMapper;
import com.winbaoxian.testng.platform.model.mapper.PlatformTestReportMapper;
import com.winbaoxian.testng.platform.repository.PlatformTestReportDetailsRepository;
import com.winbaoxian.testng.platform.repository.PlatformTestReportRepository;
import com.winbaoxian.testng.platform.service.helper.DataWrapHelper;
import com.winbaoxian.testng.platform.utils.TransformerUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 12:01
 */
@Service
public class ReportService {

    @Resource
    private PlatformTestReportRepository platformTestReportRepository;
    @Resource
    private PlatformTestReportDetailsRepository platformTestReportDetailsRepository;
    @Resource
    private DataWrapHelper dataWrapHelper;

    public PaginationDTO<PlatformTestReportDTO> page(TestReportQueryParams params, Pagination pagination) {
        if (params == null) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        Pageable pageable = Pagination.createPageable(pagination, WinTestNGPlatformConstant.SORT_COLUMN_START_TIME, Sort.Direction.DESC.name());
        Specification<TestReportEntity> specification = (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (params.getExecutorUid() != null) {
                predicateList.add(cb.equal(root.get("executorUid"), params.getExecutorUid()));
            }
            if (params.getRunState() != null) {
                predicateList.add(cb.equal(root.get("runState"), params.getRunState()));
            }
            if (params.getTriggerMode() != null) {
                predicateList.add(cb.equal(root.get("triggerMode"), params.getTriggerMode()));
            }
            if (params.getStartTime() != null) {
                predicateList.add(cb.greaterThanOrEqualTo(root.get("startTime"), new Date(params.getStartTime())));
            }
            if (params.getEndTime() != null) {
                predicateList.add(cb.lessThan(root.get("startTime"), new Date(params.getEndTime())));
            }
            if ((params.getTestCasesId() != null) || (params.getModuleId() != null) || (params.getProjectId() != null)) {
                Subquery<TestReportDetailsEntity> subQuery = query.subquery(TestReportDetailsEntity.class);
                Root<TestReportDetailsEntity> subRoot = subQuery.from(TestReportDetailsEntity.class);
                if (params.getTestCasesId() != null) {
                    subQuery.select(subRoot).where(cb.equal(subRoot.get("reportUuid"), root.get("reportUuid")),
                            cb.equal(subRoot.get("testCasesId"), params.getTestCasesId()));
                } else if (params.getModuleId() != null) {
                    Root<TestCasesEntity> subRoot2 = subQuery.from(TestCasesEntity.class);
                    subQuery.select(subRoot).where(cb.equal(subRoot.get("reportUuid"), root.get("reportUuid")),
                            cb.equal(subRoot.get("testCasesId"), subRoot2.get("id")),
                            cb.equal(subRoot2.get("moduleId"), params.getModuleId()));
                } else if (params.getProjectId() != null) {
                    Root<TestCasesEntity> subRoot2 = subQuery.from(TestCasesEntity.class);
                    subQuery.select(subRoot).where(cb.equal(subRoot.get("reportUuid"), root.get("reportUuid")),
                            cb.equal(subRoot.get("testCasesId"), subRoot2.get("id")),
                            cb.equal(subRoot2.get("projectId"), params.getProjectId()));
                }
                predicateList.add(cb.exists(subQuery));
            }
            predicateList.add(cb.isFalse(root.get("deleted")));
            return query.where(predicateList.toArray(new Predicate[predicateList.size()])).getRestriction();
        };
        Page<TestReportEntity> page = platformTestReportRepository.findAll(specification, pageable);
        List<PlatformTestReportDTO> dtoList = PlatformTestReportMapper.INSTANCE.toDTOList(page.getContent());
        dataWrapHelper.wrapTestReportList(dtoList);
        return PaginationDTO.createNewInstance(pagination, (int) page.getTotalElements(), dtoList);
    }

    public PlatformTestReportDTO getTestReport(String reportUuid) {
        if (StringUtils.isBlank(reportUuid)) {
            return null;
        }
        TestReportEntity entity = platformTestReportRepository.findByReportUuidAndDeletedFalse(reportUuid);
        return PlatformTestReportMapper.INSTANCE.toDTO(entity);
    }

    public List<PlatformTestReportDetailsDTO> getTestReportDetailsList(String reportUuid) {
        if (StringUtils.isBlank(reportUuid)) {
            return null;
        }
        List<Map<String, Object>> dtoMapList = platformTestReportDetailsRepository.findByReportUuidAndDeletedFalseOrderByCreateTimeAsc(reportUuid);
        List<PlatformTestReportDetailsDTO> dtoList = TransformerUtils.INSTANCE.transformMapList2ObjectList(dtoMapList, PlatformTestReportDetailsDTO.class);
        dataWrapHelper.wrapTestReportDetailsList(dtoList);
        return dtoList;
    }

    public PlatformTestReportDetailsDTO getTestReportDetails(String reportUuid, Long testCasesId) {
        if (StringUtils.isBlank(reportUuid) || testCasesId == null) {
            return null;
        }
        TestReportDetailsEntity entity = platformTestReportDetailsRepository.findByReportUuidAndTestCasesId(reportUuid, testCasesId);
        PlatformTestReportDetailsDTO dto = PlatformTestReportDetailsMapper.INSTANCE.toDTO(entity);
        dataWrapHelper.wrapTestReportDetails(dto);
        return dto;
    }

    public List<PlatformTestReportDetailsDTO> getSimpleTestReportDetailsList(List<String> reportUuidList) {
        if (CollectionUtils.isEmpty(reportUuidList)) {
            return null;
        }
        List<Map<String, Object>> entityList = platformTestReportDetailsRepository.findSimpleListByReportUuidInAndDeletedFalse(reportUuidList);
        List<PlatformTestReportDetailsDTO> dtoList = TransformerUtils.INSTANCE.transformMapList2ObjectList(entityList, PlatformTestReportDetailsDTO.class);
        dataWrapHelper.wrapTestReportDetailsList(dtoList);
        return dtoList;
    }

    @Transactional
    public void chooseFailReason(ReportChooseFailReasonParams params) {
        if (params == null || params.getId() == null || params.getFailReasonId() == null) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        TestReportEntity entity = platformTestReportRepository.findOne(params.getId());
        if (entity == null) {
            throw new WinTestNgPlatformException("数据不存在");
        }
        entity.setFailReasonId(params.getFailReasonId());
        platformTestReportRepository.save(entity);
    }

    @Transactional
    public void chooseFixFlag(ReportChooseFixFlagParams params) {
        if (params == null || params.getId() == null || params.getFixFlag() == null) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        TestReportEntity entity = platformTestReportRepository.findOne(params.getId());
        if (entity == null) {
            throw new WinTestNgPlatformException("数据不存在");
        }
        entity.setFixFlag(params.getFixFlag());
        platformTestReportRepository.save(entity);
    }

}
