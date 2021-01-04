package com.winbaoxian.testng.platform.model.mapper;

import com.winbaoxian.testng.model.entity.TestReportEntity;
import com.winbaoxian.testng.platform.model.dto.PlatformTestReportDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 18:04
 */

@Mapper(componentModel = "spring")
public interface PlatformTestReportMapper {

    PlatformTestReportMapper INSTANCE = Mappers.getMapper(PlatformTestReportMapper.class);

    List<PlatformTestReportDTO> toDTOList(List<TestReportEntity> content);

    PlatformTestReportDTO toDTO(TestReportEntity entity);
}
