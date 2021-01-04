package com.winbaoxian.testng.platform.service;

import com.winbaoxian.module.security.annotation.EnableSysLog;
import com.winbaoxian.testng.model.entity.GlobalParamsEntity;
import com.winbaoxian.testng.platform.exception.WinTestNgPlatformException;
import com.winbaoxian.testng.platform.model.common.Pagination;
import com.winbaoxian.testng.platform.model.common.PaginationDTO;
import com.winbaoxian.testng.platform.model.constant.WinTestNGPlatformConstant;
import com.winbaoxian.testng.platform.model.dto.PlatformGlobalParamsDTO;
import com.winbaoxian.testng.platform.model.mapper.PlatformGlobalParamsMapper;
import com.winbaoxian.testng.platform.repository.PlatformGlobalParamsRepository;
import com.winbaoxian.testng.platform.utils.BeanMergeUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 12:04
 */
@Service
public class GlobalParamsService {

    @Resource
    private PlatformGlobalParamsRepository platformGlobalParamsRepository;

    @Transactional
    @EnableSysLog
    public void save(PlatformGlobalParamsDTO dto) {
        if (dto == null || StringUtils.isBlank(dto.getKey()) || StringUtils.isBlank(dto.getValue())) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        if (dto.getId() == null) {
            //重复的key不允许添加
            if (platformGlobalParamsRepository.existsByKeyAndDeletedFalse(dto.getKey())) {
                throw new WinTestNgPlatformException("键值重复，请重新操作");
            }
            GlobalParamsEntity entity = PlatformGlobalParamsMapper.INSTANCE.toEntity(dto);
            platformGlobalParamsRepository.save(entity);
        } else {
            //重复的key不允许添加
            if (platformGlobalParamsRepository.existsByKeyAndIdNotAndDeletedFalse(dto.getKey(),dto.getId())) {
                throw new WinTestNgPlatformException("键值重复，请重新操作");
            }
            GlobalParamsEntity persistent = platformGlobalParamsRepository.findOne(dto.getId());
            if (persistent == null || BooleanUtils.isTrue(persistent.getDeleted())) {
                throw new WinTestNgPlatformException("数据不存在或已删除");
            }
            BeanMergeUtils.INSTANCE.copyProperties(dto, persistent);
            platformGlobalParamsRepository.save(persistent);
        }
    }

    public PaginationDTO<PlatformGlobalParamsDTO> page(String key, Pagination pagination) {
        if (key == null) {
            key = StringUtils.EMPTY;
        }
        Pageable pageable = Pagination.createPageable(pagination, WinTestNGPlatformConstant.SORT_COLUMN_CREATE_TIME, Sort.Direction.DESC.name());
        Page<GlobalParamsEntity> page = platformGlobalParamsRepository.findByKeyContainsAndDeletedFalse(key, pageable);
        return PaginationDTO.createNewInstance(page, PlatformGlobalParamsDTO.class);
    }

    @Transactional
    @EnableSysLog
    public void delete(Long id) {
        if (id == null) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        GlobalParamsEntity entity = platformGlobalParamsRepository.findOne(id);
        if (entity == null || BooleanUtils.isTrue(entity.getDeleted())) {
            throw new WinTestNgPlatformException("数据不存在或已删除");
        }
        entity.setDeleted(true);
        platformGlobalParamsRepository.save(entity);
    }

}
