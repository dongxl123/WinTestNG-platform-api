package com.winbaoxian.testng.platform.model.mapper;

import com.winbaoxian.testng.model.dto.ApiDTO;
import com.winbaoxian.testng.model.entity.ApiEntity;
import com.winbaoxian.testng.platform.model.dto.ExcelApiDTO;
import com.winbaoxian.testng.platform.model.dto.PlatformApiDTO;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 18:04
 */

@Mapper(componentModel = "spring", imports = {BooleanUtils.class, StringUtils.class})
public interface PlatformApiMapper {

    PlatformApiMapper INSTANCE = Mappers.getMapper(PlatformApiMapper.class);

    ApiEntity toEntity(PlatformApiDTO dto);

    PlatformApiDTO toDTO(ApiEntity entity);

    List<PlatformApiDTO> toDTOList(List<ApiEntity> entityList);

    @Mappings({@Mapping(target = "targetFlag", expression = "java(StringUtils.equals(\"是\",dto.getTargetFlag())?true:false)"),
            @Mapping(target = "finishFlag", expression = "java(StringUtils.equals(\"是\",dto.getFinishFlag())?true:false)")})
    ApiEntity toEntity(ExcelApiDTO dto);

    @Mappings({@Mapping(target = "targetFlag", expression = "java(BooleanUtils.isTrue(entity.getTargetFlag())?\"是\":\"否\")"),
            @Mapping(target = "finishFlag", expression = "java(BooleanUtils.isTrue(entity.getFinishFlag())?\"是\":\"否\")")})
    ExcelApiDTO toExcelDTO(ApiEntity entity);

    List<ExcelApiDTO> toExcelDTOList(List<ApiEntity> entityList);

    List<ApiEntity> toEntityList(List<ApiDTO> apiDTOList);
}
