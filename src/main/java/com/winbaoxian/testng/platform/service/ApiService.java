package com.winbaoxian.testng.platform.service;

import com.alibaba.excel.EasyExcel;
import com.winbaoxian.testng.model.entity.ApiEntity;
import com.winbaoxian.testng.model.entity.ProjectEntity;
import com.winbaoxian.testng.platform.exception.WinTestNgPlatformException;
import com.winbaoxian.testng.platform.model.common.Pagination;
import com.winbaoxian.testng.platform.model.common.PaginationDTO;
import com.winbaoxian.testng.platform.model.constant.WinTestNGPlatformConstant;
import com.winbaoxian.testng.platform.model.dto.ApiSearchParams;
import com.winbaoxian.testng.platform.model.dto.ExcelApiDTO;
import com.winbaoxian.testng.platform.model.dto.PlatformApiDTO;
import com.winbaoxian.testng.platform.model.mapper.PlatformApiMapper;
import com.winbaoxian.testng.platform.repository.PlatformApiRepository;
import com.winbaoxian.testng.platform.repository.PlatformProjectRepository;
import com.winbaoxian.testng.platform.utils.BeanMergeUtils;
import com.winbaoxian.testng.utils.UrlParserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * (Api)Service类
 *
 * @author dongxuanliang252
 * @date 2020-05-26 09:20:24
 */
@Service
@Slf4j
public class ApiService {

    @Resource
    private PlatformApiRepository platformApiRepository;
    @Resource
    private PlatformProjectRepository platformProjectRepository;

    @Transactional
    public void save(PlatformApiDTO dto) {
        if (dto == null) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        if (dto.getProjectId() == null) {
            throw new WinTestNgPlatformException("缺少参数projectId");
        }
        if (StringUtils.isBlank(dto.getApiUrl())) {
            throw new WinTestNgPlatformException("缺少参数apiUrl");
        }
        //统一处理url, 保证规则一致，防止重复数据
        String apiUrl = UrlParserUtils.INSTANCE.parseStrictRequestPath(dto.getApiUrl());
        dto.setApiUrl(apiUrl);
        if (dto.getId() == null) {
            if (platformApiRepository.existsByProjectIdAndApiUrlAndDeletedFalse(dto.getProjectId(), apiUrl)) {
                throw new WinTestNgPlatformException("接口地址已存在，不允许重复添加");
            }
            ApiEntity entity = PlatformApiMapper.INSTANCE.toEntity(dto);
            platformApiRepository.save(entity);
        } else {
            ApiEntity persistent = platformApiRepository.findOne(dto.getId());
            if (persistent == null || BooleanUtils.isTrue(persistent.getDeleted())) {
                throw new WinTestNgPlatformException("数据不存在或已删除");
            }
            if (platformApiRepository.existsByProjectIdAndApiUrlAndIdNotAndDeletedFalse(dto.getProjectId(), apiUrl, dto.getId())) {
                throw new WinTestNgPlatformException("接口地址已存在，不允许重复添加");
            }
            BeanMergeUtils.INSTANCE.copyProperties(dto, persistent);
            platformApiRepository.save(persistent);
        }
    }

    public PaginationDTO<PlatformApiDTO> page(ApiSearchParams params, Pagination pagination) {
        if (params == null) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        if (params.getProjectId() == null) {
            throw new WinTestNgPlatformException("缺少参数projectId");
        }
        Specification<ApiEntity> specification = (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (params.getProjectId() != null) {
                predicateList.add(cb.equal(root.get("projectId"), params.getProjectId()));
            }
            if (params.getTargetFlag() != null) {
                predicateList.add(cb.equal(root.get("targetFlag"), params.getTargetFlag()));
            }
            if (params.getFinishFlag() != null) {
                predicateList.add(cb.equal(root.get("finishFlag"), params.getFinishFlag()));
            }
            if (StringUtils.isNotBlank(params.getSearchKey())) {
                predicateList.add(cb.or(cb.like(root.get("apiUrl"), "%" + params.getSearchKey() + "%"), cb.like(root.get("apiTitle"), "%" + params.getSearchKey() + "%")));
            }
            predicateList.add(cb.isFalse(root.get("deleted")));
            return query.where(predicateList.toArray(new Predicate[predicateList.size()])).getRestriction();
        };
        Pageable pageable = Pagination.createPageable(pagination, WinTestNGPlatformConstant.SORT_COLUMN_CREATE_TIME, Sort.Direction.DESC.name());
        Page<ApiEntity> page = platformApiRepository.findAll(specification, pageable);
        return PaginationDTO.createNewInstance(page, PlatformApiDTO.class);
    }

    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new WinTestNgPlatformException("参数不正确");
        }
        ApiEntity entity = platformApiRepository.findOne(id);
        if (entity == null || BooleanUtils.isTrue(entity.getDeleted())) {
            throw new WinTestNgPlatformException("数据不存在或已删除");
        }
        entity.setDeleted(true);
        platformApiRepository.save(entity);
    }

    @Transactional
    public void upload(Long projectId, MultipartFile file) {
        if (projectId == null) {
            throw new WinTestNgPlatformException("缺少参数projectId");
        }
        if (file == null) {
            throw new WinTestNgPlatformException("请上传文件");
        }
        ProjectEntity entity = platformProjectRepository.findOne(projectId);
        if (entity == null) {
            throw new WinTestNgPlatformException("数据不存在");
        }
        List<ExcelApiDTO> list = null;
        try {
            list = EasyExcel.read(file.getInputStream()).head(ExcelApiDTO.class).sheet().doReadSync();
        } catch (Exception e) {
            log.error("api data upload error", e);
            throw new WinTestNgPlatformException("读取文件出错");
        }
        if (CollectionUtils.isEmpty(list)) {
            throw new WinTestNgPlatformException("未获取到数据");
        }
        List<ApiEntity> entityList = new ArrayList<>();
        Set<String> urlSet = new HashSet<>();
        for (ExcelApiDTO dto : list) {
            if (StringUtils.isBlank(dto.getApiUrl())) {
                continue;
            }
            //统一处理url, 保证规则一致，防止重复数据
            String apiUrl = UrlParserUtils.INSTANCE.parseStrictRequestPath(dto.getApiUrl());
            dto.setApiUrl(apiUrl);
            if (urlSet.contains(apiUrl)) {
                throw new WinTestNgPlatformException(String.format("文件中有重复的url地址:%s", apiUrl));
            }
            urlSet.add(apiUrl);
            ApiEntity newEntity = PlatformApiMapper.INSTANCE.toEntity(dto);
            newEntity.setFinishFlag(Boolean.FALSE);
            newEntity.setProjectId(projectId);
            newEntity.setId(null);
            if (dto.getId() != null) {
                ApiEntity persistent = platformApiRepository.findByProjectIdAndIdAndDeletedFalse(projectId, dto.getId());
                if (persistent != null) {
                    BeanMergeUtils.INSTANCE.copyProperties(newEntity, persistent, "finishFlag");
                    entityList.add(persistent);
                } else {
                    entityList.add(newEntity);
                }
            } else {
                ApiEntity persistent = platformApiRepository.findByProjectIdAndApiUrlAndDeletedFalse(projectId, dto.getApiUrl());
                if (persistent != null) {
                    BeanMergeUtils.INSTANCE.copyProperties(newEntity, persistent, "finishFlag");
                    entityList.add(persistent);
                } else {
                    entityList.add(newEntity);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(entityList)) {
            platformApiRepository.save(entityList);
        }
    }

    public void export(ApiSearchParams params) {
        if (params == null) {
            throw new WinTestNgPlatformException("参数不能为空");
        }
        if (params.getProjectId() == null) {
            throw new WinTestNgPlatformException("缺少参数projectId");
        }
        Specification<ApiEntity> specification = (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (params.getProjectId() != null) {
                predicateList.add(cb.equal(root.get("projectId"), params.getProjectId()));
            }
            if (params.getTargetFlag() != null) {
                predicateList.add(cb.equal(root.get("targetFlag"), params.getTargetFlag()));
            }
            if (params.getFinishFlag() != null) {
                predicateList.add(cb.equal(root.get("finishFlag"), params.getFinishFlag()));
            }
            if (StringUtils.isNotBlank(params.getSearchKey())) {
                predicateList.add(cb.or(cb.like(root.get("apiUrl"), "%" + params.getSearchKey() + "%"), cb.like(root.get("apiTitle"), "%" + params.getSearchKey() + "%")));
            }
            predicateList.add(cb.isFalse(root.get("deleted")));
            return query.where(predicateList.toArray(new Predicate[predicateList.size()])).getRestriction();
        };
        Sort sort = new Sort(Sort.Direction.DESC, WinTestNGPlatformConstant.SORT_COLUMN_CREATE_TIME);
        List<ApiEntity> list = platformApiRepository.findAll(specification, sort);
        List<ExcelApiDTO> dataList = PlatformApiMapper.INSTANCE.toExcelDTOList(list);
        ProjectEntity projectEntity = platformProjectRepository.findOne(params.getProjectId());
        String fName = String.format("%s-接口列表数据", projectEntity == null ? params.getProjectId() : projectEntity.getName());
        try {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = requestAttributes.getResponse();
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode(fName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), ExcelApiDTO.class).sheet("data").doWrite(dataList);
        } catch (Exception e) {
            log.error("download error", e);
            throw new WinTestNgPlatformException("下载文件出错");
        }
    }

}