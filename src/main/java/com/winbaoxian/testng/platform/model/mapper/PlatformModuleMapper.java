package com.winbaoxian.testng.platform.model.mapper;

import com.winbaoxian.testng.model.entity.ModuleEntity;
import com.winbaoxian.testng.platform.model.dto.ModuleSelectDTO;
import com.winbaoxian.testng.platform.model.dto.PlatformModuleDTO;
import com.winbaoxian.testng.platform.model.dto.PlatformProjectDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 18:04
 */

@Mapper(componentModel = "spring")
public interface PlatformModuleMapper {

    PlatformModuleMapper INSTANCE = Mappers.getMapper(PlatformModuleMapper.class);

    ModuleEntity toEntity(PlatformModuleDTO dto);

    List<PlatformModuleDTO> toDTOList(List<ModuleEntity> entityList);

    List<ModuleSelectDTO> toSelectDTOList(List<ModuleEntity> entityList);

    PlatformModuleDTO toDTO(ModuleEntity entity);
}
