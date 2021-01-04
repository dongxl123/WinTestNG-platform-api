package com.winbaoxian.testng.platform.repository;

import com.winbaoxian.testng.model.entity.TestReportDetailsEntity;
import com.winbaoxian.testng.repository.TestReportDetailsRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 11:50
 */
public interface PlatformTestReportDetailsRepository extends TestReportDetailsRepository {

    @Query(value = "SELECT new map(a.id as id, a.createTime as createTime, a.updateTime as updateTime, a.reportUuid as reportUuid, a.testCasesId as testCasesId, a.startTime as startTime, a.endTime as endTime, a.duration as duration, a.totalCount as totalCount, a.successCount as successCount, a.failCount as failCount, a.runState as runState, a.deleted as deleted, (a.exceptions is not null) as hasExceptions) FROM TestReportDetailsEntity a WHERE a.reportUuid = ?1 AND a.deleted = 0 ORDER BY a.createTime ASC")
    List<Map<String, Object>> findByReportUuidAndDeletedFalseOrderByCreateTimeAsc(String reportUuid);

    TestReportDetailsEntity findByReportUuidAndTestCasesId(String reportUuid, Long testCasesId);

    @Query("select new map(a.id as id, a.reportUuid as reportUuid, a.testCasesId as testCasesId) from TestReportDetailsEntity a where a.reportUuid in ?1 and a.deleted=false ")
    List<Map<String, Object>> findSimpleListByReportUuidInAndDeletedFalse(List<String> reportUuidList);
}
