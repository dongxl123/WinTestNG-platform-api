package com.winbaoxian.testng.platform.model.mapper;

import com.alibaba.fastjson.JSON;
import com.winbaoxian.testng.model.entity.ActionTemplateEntity;
import com.winbaoxian.testng.model.mapper.ActionTemplateMapper;
import com.winbaoxian.testng.platform.model.dto.ActionTemplateSelectDTO;
import com.winbaoxian.testng.platform.model.dto.PlatformActionTemplateDTO;
import com.winbaoxian.testng.utils.ConfigParseUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 18:04
 */

@Mapper(componentModel = "spring", imports = {JSON.class, ConfigParseUtils.class})
public interface PlatformActionTemplateMapper {

    PlatformActionTemplateMapper INSTANCE = Mappers.getMapper(PlatformActionTemplateMapper.class);

    default ActionTemplateEntity toEntity(PlatformActionTemplateDTO dto) {
        return ActionTemplateMapper.INSTANCE.toEntity(dto);
    }

    @Mappings({
            @Mapping(expression = "java(ConfigParseUtils.INSTANCE.parseActionSettingList(entity.getActions()))", target = "actions")
    })
    PlatformActionTemplateDTO toDTO(ActionTemplateEntity entity);

    List<PlatformActionTemplateDTO> toDTOList(List<ActionTemplateEntity> entityList);

    List<ActionTemplateSelectDTO> toSelectDTOList(List<ActionTemplateEntity> entityList);
}
