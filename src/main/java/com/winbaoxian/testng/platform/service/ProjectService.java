package com.winbaoxian.testng.platform.service;

import com.winbaoxian.testng.model.entity.ProjectEntity;
import com.winbaoxian.testng.platform.exception.WinTestNgPlatformException;
import com.winbaoxian.testng.platform.model.common.Pagination;
import com.winbaoxian.testng.platform.model.common.PaginationDTO;
import com.winbaoxian.testng.platform.model.constant.WinTestNGPlatformConstant;
import com.winbaoxian.testng.platform.model.dto.PlatformProjectDTO;
import com.winbaoxian.testng.platform.model.dto.ProjectSelectDTO;
import com.winbaoxian.testng.platform.model.mapper.PlatformProjectMapper;
import com.winbaoxian.testng.platform.repository.PlatformProjectRepository;
import com.winbaoxian.testng.platform.repository.PlatformTestCasesRepository;
import com.winbaoxian.testng.platform.service.helper.DataWrapHelper;
import com.winbaoxian.testng.platform.utils.BeanMergeUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
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
public class ProjectService {

    @Resource
    private PlatformProjectRepository platformProjectRepository;
    @Resource
    private PlatformTestCasesRepository platformTestCasesRepository;
    @Resource
    private SortService sortService;
    @Resource
    private DataWrapHelper dataWrapHelper;

    @Transactional
    public void save(PlatformProjectDTO dto) {
        if (dto == null || StringUtils.isBlank(dto.getName())) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        if (dto.getId() == null) {
            ProjectEntity entity = PlatformProjectMapper.INSTANCE.toEntity(dto);
            platformProjectRepository.save(entity);
        } else {
            ProjectEntity persistent = platformProjectRepository.findOne(dto.getId());
            if (persistent == null || BooleanUtils.isTrue(persistent.getDeleted())) {
                throw new WinTestNgPlatformException("数据不存在或已删除");
            }
            BeanMergeUtils.INSTANCE.copyProperties(dto, persistent);
            platformProjectRepository.save(persistent);
        }
    }

    public PaginationDTO<PlatformProjectDTO> page(String name, Boolean integrationFlag, Pagination pagination) {
        Pageable pageable = Pagination.createPageable(pagination, WinTestNGPlatformConstant.SORT_COLUMN_CREATE_TIME, Sort.Direction.DESC.name());
        Specification<ProjectEntity> specification = (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.isNotBlank(name)) {
                predicateList.add(cb.like(root.get("name"), String.format("%%%s%%", name)));
            }
            if (integrationFlag != null) {
                predicateList.add(cb.equal(root.get("integrationFlag"), integrationFlag));
            }
            predicateList.add(cb.isFalse(root.get("deleted")));
            return query.where(predicateList.toArray(new Predicate[predicateList.size()])).getRestriction();
        };
        Page<ProjectEntity> page = platformProjectRepository.findAll(specification, pageable);
        Integer count = page.getNumber();
        List<PlatformProjectDTO> list = PlatformProjectMapper.INSTANCE.toDTOList(page.getContent());
        dataWrapHelper.wrapProjectList(list);
        return PaginationDTO.createNewInstance(pagination, count, list);
    }

    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        if (platformTestCasesRepository.existsByProjectIdAndDeletedFalse(id)) {
            throw new WinTestNgPlatformException("请先删除该项目下的测试任务");
        }
        ProjectEntity entity = platformProjectRepository.findOne(id);
        if (entity == null || BooleanUtils.isTrue(entity.getDeleted())) {
            throw new WinTestNgPlatformException("数据不存在或已删除");
        }
        entity.setDeleted(true);
        platformProjectRepository.save(entity);
    }

    public List<ProjectSelectDTO> getSelectProjectList() {
        List<ProjectEntity> entityList = platformProjectRepository.findByDeletedFalseOrderByCreateTimeDesc();
        List<ProjectSelectDTO> retList = PlatformProjectMapper.INSTANCE.toSelectDTOList(entityList);
        sortService.sortSelectProjectList(retList);
        return retList;
    }

    public List<PlatformProjectDTO> getByIdList(List<Long> idList) {
        List<ProjectEntity> entityList = platformProjectRepository.findByIdIn(idList);
        return PlatformProjectMapper.INSTANCE.toDTOList(entityList);
    }

    public PlatformProjectDTO getById(Long id) {
        ProjectEntity entity = platformProjectRepository.findOne(id);
        return PlatformProjectMapper.INSTANCE.toDTO(entity);
    }

    public List<PlatformProjectDTO> getByIntegrationFlagTrue() {
        List<ProjectEntity> entityList = platformProjectRepository.findByIntegrationFlagTrueAndDeletedFalse();
        return PlatformProjectMapper.INSTANCE.toDTOList(entityList);
    }

}
