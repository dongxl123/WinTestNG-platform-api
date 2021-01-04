package com.winbaoxian.testng.platform.model.mapper;

import com.winbaoxian.testng.model.dto.TestReportDetailsDTO;
import com.winbaoxian.testng.model.entity.TestReportDetailsEntity;
import com.winbaoxian.testng.model.mapper.TestReportDetailsMapper;
import com.winbaoxian.testng.platform.model.dto.PlatformTestReportDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 18:04
 */

@Mapper(componentModel = "spring")
public interface PlatformTestReportDetailsMapper {

    PlatformTestReportDetailsMapper INSTANCE = Mappers.getMapper(PlatformTestReportDetailsMapper.class);

    PlatformTestReportDetailsDTO convertDTO(TestReportDetailsDTO dto);

    List<PlatformTestReportDetailsDTO> convertDTOList(List<TestReportDetailsDTO> dtoList);

    default List<PlatformTestReportDetailsDTO> toDTOList(List<TestReportDetailsEntity> entityList) {
        List<TestReportDetailsDTO> dtoList = TestReportDetailsMapper.INSTANCE.toDTOList(entityList);
        return convertDTOList(dtoList);
    }

    default PlatformTestReportDetailsDTO toDTO(TestReportDetailsEntity entity) {
        TestReportDetailsDTO dto = TestReportDetailsMapper.INSTANCE.toDTO(entity);
        return convertDTO(dto);
    }

}
