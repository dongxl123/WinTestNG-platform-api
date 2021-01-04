package com.winbaoxian.testng.platform.service;

import com.winbaoxian.testng.model.entity.ActionTemplateEntity;
import com.winbaoxian.testng.platform.exception.WinTestNgPlatformException;
import com.winbaoxian.testng.platform.model.common.Pagination;
import com.winbaoxian.testng.platform.model.common.PaginationDTO;
import com.winbaoxian.testng.platform.model.constant.WinTestNGPlatformConstant;
import com.winbaoxian.testng.platform.model.dto.ActionTemplateDebugParams;
import com.winbaoxian.testng.platform.model.dto.ActionTemplateSelectDTO;
import com.winbaoxian.testng.platform.model.dto.ConsoleLogDTO;
import com.winbaoxian.testng.platform.model.dto.PlatformActionTemplateDTO;
import com.winbaoxian.testng.platform.model.mapper.PlatformActionTemplateMapper;
import com.winbaoxian.testng.platform.repository.PlatformActionTemplateRepository;
import com.winbaoxian.testng.platform.service.helper.DataWrapHelper;
import com.winbaoxian.testng.platform.service.helper.PlatformTestNGHelper;
import com.winbaoxian.testng.platform.utils.BeanMergeUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class ActionTemplateService {

    @Resource
    private PlatformActionTemplateRepository platformActionTemplateRepository;
    @Resource
    private PlatformTestNGHelper platformTestNGHelper;
    @Resource
    private DataWrapHelper dataWrapHelper;

    @Transactional
    public void save(PlatformActionTemplateDTO dto) {
        if (dto == null || StringUtils.isBlank(dto.getName())) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        ActionTemplateEntity entity = PlatformActionTemplateMapper.INSTANCE.toEntity(dto);
        if (dto.getId() == null) {
            platformActionTemplateRepository.save(entity);
        } else {
            ActionTemplateEntity persistent = platformActionTemplateRepository.findOne(dto.getId());
            if (persistent == null || BooleanUtils.isTrue(persistent.getDeleted())) {
                throw new WinTestNgPlatformException("数据不存在或已删除");
            }
            BeanMergeUtils.INSTANCE.copyProperties(entity, persistent);
            platformActionTemplateRepository.save(persistent);
        }
    }

    public PaginationDTO<PlatformActionTemplateDTO> page(String name, Pagination pagination) {
        if (name == null) {
            name = StringUtils.EMPTY;
        }
        Pageable pageable = Pagination.createPageable(pagination, WinTestNGPlatformConstant.SORT_COLUMN_CREATE_TIME, Sort.Direction.DESC.name());
        Page<ActionTemplateEntity> page = platformActionTemplateRepository.findByNameContainsAndDeletedFalse(name, pageable);
        List<PlatformActionTemplateDTO> dtoList = PlatformActionTemplateMapper.INSTANCE.toDTOList(page.getContent());
        dataWrapHelper.wrapActionTemplateList(dtoList);
        return PaginationDTO.createNewInstance(pagination, (int) page.getTotalElements(), dtoList);
    }

    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        ActionTemplateEntity entity = platformActionTemplateRepository.findOne(id);
        if (entity == null || BooleanUtils.isTrue(entity.getDeleted())) {
            throw new WinTestNgPlatformException("数据不存在或已删除");
        }
        entity.setDeleted(true);
        platformActionTemplateRepository.save(entity);
    }

    public List<ActionTemplateSelectDTO> getSelectActionTemplateList() {
        List<ActionTemplateEntity> entityList = platformActionTemplateRepository.findByDeletedFalseOrderByCreateTimeDesc();
        return PlatformActionTemplateMapper.INSTANCE.toSelectDTOList(entityList);
    }

    public String debug(ActionTemplateDebugParams params) {
        if (params == null || params.getTemplateId() == null) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        return platformTestNGHelper.executeActionTemplate(params.getTemplateId(), params.getMappings());
    }

    public ConsoleLogDTO getDebugLog(String uuid, Long offset) {
        return platformTestNGHelper.getAppendLog(uuid, offset);
    }

}
