package com.winbaoxian.testng.platform.repository;

import com.winbaoxian.testng.model.entity.ProjectQualityReportHistoryEntity;
import com.winbaoxian.testng.repository.ProjectQualityReportHistoryRepository;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2020年6月24日16:53:38
 */
public interface PlatformProjectQualityReportHistoryRepository extends ProjectQualityReportHistoryRepository {

    boolean existsByDateRangeAndDeletedFalse(String dateRange);

    List<ProjectQualityReportHistoryEntity> findByDateRangeAndDeletedFalse(String dateRange);

}
