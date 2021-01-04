package com.winbaoxian.testng.platform.service;

import com.winbaoxian.module.security.annotation.EnableSysLog;
import com.winbaoxian.testng.enums.ResourceType;
import com.winbaoxian.testng.model.entity.ResourceConfigEntity;
import com.winbaoxian.testng.platform.exception.WinTestNgPlatformException;
import com.winbaoxian.testng.platform.model.common.Pagination;
import com.winbaoxian.testng.platform.model.common.PaginationDTO;
import com.winbaoxian.testng.platform.model.constant.WinTestNGPlatformConstant;
import com.winbaoxian.testng.platform.model.dto.PlatformResourceConfigDTO;
import com.winbaoxian.testng.platform.model.dto.ResourceConfigSelectDTO;
import com.winbaoxian.testng.platform.model.mapper.PlatformResourceConfigMapper;
import com.winbaoxian.testng.platform.repository.PlatformResourceConfigRepository;
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
 * @date 2019-03-20 12:06
 */
@Service
public class ResourceConfigService {

    @Resource
    private PlatformResourceConfigRepository platformResourceConfigRepository;

    @Transactional
    @EnableSysLog
    public void save(PlatformResourceConfigDTO dto) {
        if (dto == null || StringUtils.isBlank(dto.getName()) || StringUtils.isBlank(dto.getSettings()) || dto.getResourceType() == null) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        if (dto.getId() == null) {
            ResourceConfigEntity entity = PlatformResourceConfigMapper.INSTANCE.toEntity(dto);
            platformResourceConfigRepository.save(entity);
        } else {
            ResourceConfigEntity persistent = platformResourceConfigRepository.findOne(dto.getId());
            if (persistent == null || BooleanUtils.isTrue(persistent.getDeleted())) {
                throw new WinTestNgPlatformException("数据不存在或已删除");
            }
            BeanMergeUtils.INSTANCE.copyProperties(dto, persistent);
            platformResourceConfigRepository.save(persistent);
        }
    }

    public PaginationDTO<PlatformResourceConfigDTO> page(String resourceType, String name, Pagination pagination) {
        ResourceType resourceTypeEnum = null;
        if (StringUtils.isNotBlank(resourceType)) {
            try {
                resourceTypeEnum = ResourceType.valueOf(resourceType);
            } catch (Exception e) {
                throw new WinTestNgPlatformException("资源类型不存在");
            }
            if (resourceTypeEnum == null) {
                throw new WinTestNgPlatformException("资源类型不存在");
            }
        }
        String finalName = name;
        ResourceType finalResourceTypeEnum = resourceTypeEnum;
        Specification<ResourceConfigEntity> specification = (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (finalResourceTypeEnum != null) {
                predicateList.add(cb.equal(root.get("resourceType"), finalResourceTypeEnum));
            }
            if (StringUtils.isNotBlank(finalName)) {
                predicateList.add(cb.like(root.get("name"), String.format("%%%s%%", finalName)));
            }
            predicateList.add(cb.isFalse(root.get("deleted")));
            return query.where(predicateList.toArray(new Predicate[predicateList.size()])).getRestriction();
        };
        Pageable pageable = Pagination.createPageable(pagination, WinTestNGPlatformConstant.SORT_COLUMN_CREATE_TIME, Sort.Direction.DESC.name());
        Page<ResourceConfigEntity> page = platformResourceConfigRepository.findAll(specification, pageable);
        return PaginationDTO.createNewInstance(page, PlatformResourceConfigDTO.class);
    }

    @Transactional
    @EnableSysLog
    public void delete(Long id) {
        if (id == null) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        ResourceConfigEntity entity = platformResourceConfigRepository.findOne(id);
        if (entity == null || BooleanUtils.isTrue(entity.getDeleted())) {
            throw new WinTestNgPlatformException("数据不存在或已删除");
        }
        entity.setDeleted(true);
        platformResourceConfigRepository.save(entity);
    }

    public List<ResourceConfigSelectDTO> getSelectResourceConfigList() {
        List<ResourceConfigEntity> entityList = platformResourceConfigRepository.findByDeletedFalseOrderByCreateTimeDesc();
        return PlatformResourceConfigMapper.INSTANCE.toSelectDTOList(entityList);
    }
}
