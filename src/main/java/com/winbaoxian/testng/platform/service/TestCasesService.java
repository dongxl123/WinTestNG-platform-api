package com.winbaoxian.testng.platform.service;

import com.winbaoxian.testng.model.core.DataPreparationConfigDTO;
import com.winbaoxian.testng.model.entity.TestCasesEntity;
import com.winbaoxian.testng.platform.exception.WinTestNgPlatformException;
import com.winbaoxian.testng.platform.model.common.Pagination;
import com.winbaoxian.testng.platform.model.common.PaginationDTO;
import com.winbaoxian.testng.platform.model.constant.WinTestNGPlatformConstant;
import com.winbaoxian.testng.platform.model.dto.ConsoleLogDTO;
import com.winbaoxian.testng.platform.model.dto.PlatformTestCasesDTO;
import com.winbaoxian.testng.platform.model.dto.TestCasesRunParams;
import com.winbaoxian.testng.platform.model.dto.TestCasesSelectDTO;
import com.winbaoxian.testng.platform.model.mapper.PlatformTestCasesMapper;
import com.winbaoxian.testng.platform.repository.PlatformTestCasesRepository;
import com.winbaoxian.testng.platform.service.helper.DataWrapHelper;
import com.winbaoxian.testng.platform.service.helper.PlatformTestNGHelper;
import com.winbaoxian.testng.platform.utils.BeanMergeUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 12:01
 */
@Service
public class TestCasesService {

    @Resource
    private PlatformTestCasesRepository platformTestCasesRepository;
    @Resource
    private DataWrapHelper dataWrapHelper;
    @Resource
    private PlatformTestNGHelper platformTestNGHelper;
    @Value("${project.domain}")
    private String projectDomain;

    @Transactional
    public void save(PlatformTestCasesDTO dto) {
        if (dto == null || StringUtils.isBlank(dto.getName())) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        DataPreparationConfigDTO dataPreparationConfig = dto.getDataPreparationConfig();
        if (dataPreparationConfig != null && CollectionUtils.isNotEmpty(dataPreparationConfig.getFieldNameList())) {
            List<String> newFieldNameList = new ArrayList<>();
            dataPreparationConfig.getFieldNameList().forEach(o -> newFieldNameList.add(o.trim()));
            dataPreparationConfig.setFieldNameList(newFieldNameList);
        }
        TestCasesEntity entity = PlatformTestCasesMapper.INSTANCE.toEntity(dto);
        if (dto.getId() == null) {
            platformTestCasesRepository.save(entity);
        } else {
            TestCasesEntity persistent = platformTestCasesRepository.findOne(dto.getId());
            if (persistent == null || BooleanUtils.isTrue(persistent.getDeleted())) {
                throw new WinTestNgPlatformException("数据不存在或已删除");
            }
            BeanMergeUtils.INSTANCE.copyProperties(entity, persistent);
            //null时去掉数据
            persistent.setDataPreparationConfig(entity.getDataPreparationConfig());
            platformTestCasesRepository.save(persistent);
        }
    }

    public PaginationDTO<PlatformTestCasesDTO> page(PlatformTestCasesDTO params, Pagination pagination) {
        if (params == null) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        Pageable pageable = Pagination.createPageable(pagination, WinTestNGPlatformConstant.SORT_COLUMN_CREATE_TIME, Sort.Direction.DESC.name());
        Specification<TestCasesEntity> specification = (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (params.getProjectId() != null) {
                predicateList.add(cb.equal(root.get("projectId"), params.getProjectId()));
            }
            if (params.getModuleId() != null) {
                predicateList.add(cb.equal(root.get("moduleId"), params.getModuleId()));
            }
            if (params.getOwnerUid() != null) {
                predicateList.add(cb.equal(root.get("ownerUid"), params.getOwnerUid()));
            }
            if (params.getCiFlag() != null) {
                predicateList.add(cb.equal(root.get("ciFlag"), params.getCiFlag()));
            }
            if (params.getLastRunState() != null) {
                predicateList.add(cb.equal(root.get("lastRunState"), params.getLastRunState()));
            }
            if (StringUtils.isNotBlank(params.getName())) {
                predicateList.add(cb.like(root.get("name"), String.format("%%%s%%", params.getName())));
            }
            predicateList.add(cb.isFalse(root.get("deleted")));
            return query.where(predicateList.toArray(new Predicate[predicateList.size()])).getRestriction();
        };
        Page<TestCasesEntity> page = platformTestCasesRepository.findAll(specification, pageable);
        List<PlatformTestCasesDTO> dtoList = PlatformTestCasesMapper.INSTANCE.toDTOList(page.getContent());
        dataWrapHelper.wrapTestCasesList(dtoList);
        return PaginationDTO.createNewInstance(pagination, (int) page.getTotalElements(), dtoList);
    }

    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        TestCasesEntity entity = platformTestCasesRepository.findOne(id);
        if (entity == null || BooleanUtils.isTrue(entity.getDeleted())) {
            throw new WinTestNgPlatformException("数据不存在或已删除");
        }
        entity.setDeleted(true);
        platformTestCasesRepository.save(entity);
    }

    public List<TestCasesSelectDTO> getSelectTestCasesList(Long moduleId) {
        if (moduleId == null) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        List<TestCasesEntity> entityList = platformTestCasesRepository.findByModuleIdAndDeletedFalseOrderByCreateTimeDesc(moduleId);
        return PlatformTestCasesMapper.INSTANCE.toSelectDTOList(entityList);
    }

    public String run(TestCasesRunParams params) {
        if (params == null || CollectionUtils.isEmpty(params.getIdList())) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        return platformTestNGHelper.executeTestCases(params.getIdList());
    }

    public List<PlatformTestCasesDTO> getByIdList(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return null;
        }
        List<TestCasesEntity> entityList = platformTestCasesRepository.findByIdIn(idList);
        return PlatformTestCasesMapper.INSTANCE.toDTOList(entityList);
    }

    public PlatformTestCasesDTO getById(Long id) {
        if (id == null) {
            return null;
        }
        TestCasesEntity entity = platformTestCasesRepository.findOne(id);
        return PlatformTestCasesMapper.INSTANCE.toDTO(entity);
    }

    public ConsoleLogDTO getRunLog(String uuid, Long offset) {
        ConsoleLogDTO dto = platformTestNGHelper.getAppendLog(uuid, offset);
        if (dto != null && BooleanUtils.isTrue(dto.getEndFlag())) {
            dto.setReportUrl(projectDomain + "/view/report/summary/" + uuid);
        }
        return dto;
    }

    @Transactional
    public void batchOnline(TestCasesRunParams params) {
        if (params == null || CollectionUtils.isEmpty(params.getIdList())) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        List<TestCasesEntity> entityList = platformTestCasesRepository.findByIdIn(params.getIdList());
        entityList.stream().forEach(o -> o.setCiFlag(true));
        platformTestCasesRepository.save(entityList);
    }

    @Transactional
    public void batchOffline(TestCasesRunParams params) {
        if (params == null || CollectionUtils.isEmpty(params.getIdList())) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        List<TestCasesEntity> entityList = platformTestCasesRepository.findByIdIn(params.getIdList());
        entityList.stream().forEach(o -> o.setCiFlag(false));
        platformTestCasesRepository.save(entityList);
    }

}
