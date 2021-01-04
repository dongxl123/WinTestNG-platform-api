package com.winbaoxian.testng.platform.model.mapper;

import com.winbaoxian.testng.model.entity.ProjectQualityReportHistoryEntity;
import com.winbaoxian.testng.platform.model.dto.PlatformProjectQualityReportHistoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2020年6月24日16:57:18
 */

@Mapper(componentModel = "spring")
public interface PlatformProjectQualityReportHistoryMapper {

    PlatformProjectQualityReportHistoryMapper INSTANCE = Mappers.getMapper(PlatformProjectQualityReportHistoryMapper.class);

    List<PlatformProjectQualityReportHistoryDTO> toDTOList(List<ProjectQualityReportHistoryEntity> entityList);

    PlatformProjectQualityReportHistoryDTO toDTO(ProjectQualityReportHistoryEntity entity);

    List<ProjectQualityReportHistoryEntity> toEntityList(List<PlatformProjectQualityReportHistoryDTO> dtoList);
}
