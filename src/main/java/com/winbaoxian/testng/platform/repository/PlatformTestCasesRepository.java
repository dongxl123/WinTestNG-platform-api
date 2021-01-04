package com.winbaoxian.testng.platform.repository;

import com.winbaoxian.testng.model.entity.TestCasesEntity;
import com.winbaoxian.testng.platform.model.dto.IdCountDTO;
import com.winbaoxian.testng.repository.TestCasesRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 14:43
 */
public interface PlatformTestCasesRepository extends TestCasesRepository {

    boolean existsByProjectIdAndDeletedFalse(Long projectId);

    boolean existsByModuleIdAndDeletedFalse(Long moduleId);

    boolean existsByProjectIdAndCiFlagTrueAndDeletedFalse(Long projectId);

    List<TestCasesEntity> findByModuleIdAndDeletedFalseOrderByCreateTimeDesc(Long moduleId);

    @Query(value = "SELECT new com.winbaoxian.testng.platform.model.dto.IdCountDTO(et.projectId, count(et)) FROM TestCasesEntity et WHERE et.deleted = FALSE GROUP BY et.projectId")
    List<IdCountDTO> getTestCaseCountListGroupByProductId();

    @Query(value = "SELECT new com.winbaoxian.testng.platform.model.dto.IdCountDTO(et.ownerUid, count(et)) FROM TestCasesEntity et WHERE et.deleted = FALSE GROUP BY et.ownerUid")
    List<IdCountDTO> getTestCaseCountListGroupByUserId();

}
