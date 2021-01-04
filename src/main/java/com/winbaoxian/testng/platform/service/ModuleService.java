package com.winbaoxian.testng.platform.service;

import com.winbaoxian.testng.model.entity.ModuleEntity;
import com.winbaoxian.testng.platform.exception.WinTestNgPlatformException;
import com.winbaoxian.testng.platform.model.constant.WinTestNGPlatformConstant;
import com.winbaoxian.testng.platform.model.dto.ModuleSelectDTO;
import com.winbaoxian.testng.platform.model.dto.PlatformModuleDTO;
import com.winbaoxian.testng.platform.model.mapper.PlatformModuleMapper;
import com.winbaoxian.testng.platform.repository.PlatformModuleRepository;
import com.winbaoxian.testng.platform.repository.PlatformProjectRepository;
import com.winbaoxian.testng.platform.repository.PlatformTestCasesRepository;
import com.winbaoxian.testng.platform.utils.BeanMergeUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 12:01
 */
@Service
public class ModuleService {

    @Resource
    private PlatformModuleRepository platformModuleRepository;
    @Resource
    private PlatformProjectRepository platformProjectRepository;
    @Resource
    private PlatformTestCasesRepository platformTestCasesRepository;

    @Transactional
    public void save(PlatformModuleDTO dto) {
        if (dto == null || StringUtils.isBlank(dto.getName()) || dto.getProjectId() == null) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        if (!platformProjectRepository.existsByIdAndDeletedFalse(dto.getProjectId())) {
            throw new WinTestNgPlatformException("项目不存在或已删除");
        }
        if (dto.getId() == null) {
            ModuleEntity entity = PlatformModuleMapper.INSTANCE.toEntity(dto);
            platformModuleRepository.save(entity);
        } else {
            ModuleEntity persistent = platformModuleRepository.findOne(dto.getId());
            if (persistent == null || BooleanUtils.isTrue(persistent.getDeleted())) {
                throw new WinTestNgPlatformException("数据不存在或已删除");
            }
            BeanMergeUtils.INSTANCE.copyProperties(dto, persistent);
            platformModuleRepository.save(persistent);
        }
    }

    public List<PlatformModuleDTO> list(Long projectId) {
        if (projectId == null) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        Sort sort = new Sort(Sort.Direction.DESC, WinTestNGPlatformConstant.SORT_COLUMN_CREATE_TIME);
        List<ModuleEntity> entityList = platformModuleRepository.findByProjectIdAndDeletedFalse(projectId, sort);
        return PlatformModuleMapper.INSTANCE.toDTOList(entityList);
    }

    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        if (platformTestCasesRepository.existsByModuleIdAndDeletedFalse(id)) {
            throw new WinTestNgPlatformException("请先删除该模块下的测试任务");
        }
        ModuleEntity entity = platformModuleRepository.findOne(id);
        if (entity == null || BooleanUtils.isTrue(entity.getDeleted())) {
            throw new WinTestNgPlatformException("数据不存在或已删除");
        }
        entity.setDeleted(true);
        platformModuleRepository.save(entity);
    }

    public List<ModuleSelectDTO> getSelectModuleList(Long projectId) {
        if (projectId == null) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        Sort sort = new Sort(Sort.Direction.DESC, WinTestNGPlatformConstant.SORT_COLUMN_CREATE_TIME);
        List<ModuleEntity> entityList = platformModuleRepository.findByProjectIdAndDeletedFalse(projectId, sort);
        return PlatformModuleMapper.INSTANCE.toSelectDTOList(entityList);
    }

    public List<PlatformModuleDTO> getByIdList(List<Long> idList) {
        List<ModuleEntity> entityList = platformModuleRepository.findByIdIn(idList);
        return PlatformModuleMapper.INSTANCE.toDTOList(entityList);
    }

    public PlatformModuleDTO getById(Long id) {
        ModuleEntity entity = platformModuleRepository.findOne(id);
        return PlatformModuleMapper.INSTANCE.toDTO(entity);
    }

}
