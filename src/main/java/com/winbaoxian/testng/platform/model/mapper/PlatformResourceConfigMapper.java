package com.winbaoxian.testng.platform.model.mapper;

import com.winbaoxian.testng.model.entity.ResourceConfigEntity;
import com.winbaoxian.testng.platform.model.dto.PlatformResourceConfigDTO;
import com.winbaoxian.testng.platform.model.dto.ResourceConfigSelectDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 18:04
 */

@Mapper(componentModel = "spring")
public interface PlatformResourceConfigMapper {

    PlatformResourceConfigMapper INSTANCE = Mappers.getMapper(PlatformResourceConfigMapper.class);

    ResourceConfigEntity toEntity(PlatformResourceConfigDTO dto);

    List<PlatformResourceConfigDTO> toDTOList(List<ResourceConfigEntity> entityList);

    List<ResourceConfigSelectDTO> toSelectDTOList(List<ResourceConfigEntity> entityList);
}
