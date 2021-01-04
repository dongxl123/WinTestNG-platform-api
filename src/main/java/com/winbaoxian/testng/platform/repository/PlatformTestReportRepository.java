package com.winbaoxian.testng.platform.repository;

import com.winbaoxian.testng.model.entity.TestReportEntity;
import com.winbaoxian.testng.repository.TestReportRepository;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 11:50
 */
public interface PlatformTestReportRepository extends TestReportRepository {

    TestReportEntity findByReportUuidAndDeletedFalse(String reportUuid);
}
