package com.winbaoxian.testng.platform.model.mapper;

import com.winbaoxian.testng.model.dto.TestCasesDTO;
import com.winbaoxian.testng.model.entity.ModuleEntity;
import com.winbaoxian.testng.model.entity.TestCasesEntity;
import com.winbaoxian.testng.model.mapper.TestCasesMapper;
import com.winbaoxian.testng.platform.model.dto.PlatformTestCasesDTO;
import com.winbaoxian.testng.platform.model.dto.TestCasesSelectDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 18:04
 */

@Mapper(componentModel = "spring")
public interface PlatformTestCasesMapper {

    PlatformTestCasesMapper INSTANCE = Mappers.getMapper(PlatformTestCasesMapper.class);

    default TestCasesEntity toEntity(PlatformTestCasesDTO dto) {
        return TestCasesMapper.INSTANCE.toEntity(dto);
    }

    List<PlatformTestCasesDTO> convertDTOList(List<TestCasesDTO> dtoList);

    default List<PlatformTestCasesDTO> toDTOList(List<TestCasesEntity> entityList) {
        List<TestCasesDTO> dtoList = TestCasesMapper.INSTANCE.toDTOList(entityList);
        return convertDTOList(dtoList);
    }

    List<TestCasesSelectDTO> toSelectDTOList(List<TestCasesEntity> entityList);

    PlatformTestCasesDTO convertDTO(TestCasesDTO dto);

    default PlatformTestCasesDTO toDTO(TestCasesEntity entity) {
        TestCasesDTO dto = TestCasesMapper.INSTANCE.toDTO(entity);
        return convertDTO(dto);
    }

}
