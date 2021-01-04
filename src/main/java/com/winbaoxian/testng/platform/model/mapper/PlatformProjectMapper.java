package com.winbaoxian.testng.platform.model.mapper;

import com.winbaoxian.testng.model.entity.ProjectEntity;
import com.winbaoxian.testng.platform.model.dto.PlatformProjectDTO;
import com.winbaoxian.testng.platform.model.dto.ProjectSelectDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 18:04
 */

@Mapper(componentModel = "spring")
public interface PlatformProjectMapper {

    PlatformProjectMapper INSTANCE = Mappers.getMapper(PlatformProjectMapper.class);

    ProjectEntity toEntity(PlatformProjectDTO dto);

    PlatformProjectDTO toDTO(ProjectEntity entity);

    List<PlatformProjectDTO> toDTOList(List<ProjectEntity> entityList);

    List<ProjectSelectDTO> toSelectDTOList(List<ProjectEntity> entityList);

}
