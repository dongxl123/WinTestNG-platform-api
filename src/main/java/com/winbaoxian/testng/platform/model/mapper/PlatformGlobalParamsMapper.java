package com.winbaoxian.testng.platform.model.mapper;

import com.winbaoxian.testng.model.entity.GlobalParamsEntity;
import com.winbaoxian.testng.model.entity.ProjectEntity;
import com.winbaoxian.testng.platform.model.dto.PlatformGlobalParamsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 18:04
 */

@Mapper(componentModel = "spring")
public interface PlatformGlobalParamsMapper {

    PlatformGlobalParamsMapper INSTANCE = Mappers.getMapper(PlatformGlobalParamsMapper.class);

    GlobalParamsEntity toEntity(PlatformGlobalParamsDTO dto);

    List<PlatformGlobalParamsDTO> toDTOList(List<GlobalParamsEntity> entityList);
}
