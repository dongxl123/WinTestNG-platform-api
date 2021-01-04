package com.winbaoxian.testng.platform.repository;

import com.winbaoxian.testng.platform.model.common.Pagination;
import com.winbaoxian.testng.platform.model.dto.*;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2020-01-03 14:48
 */
@Repository
public class PlatformDashboardRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<DashboardChartItemDTO> getProjectIntegrationItemList() {
        String sql = "SELECT IF(integration_flag = 1,'已集成','未集成') title , count(*) value FROM project WHERE deleted = FALSE GROUP BY integration_flag ORDER BY integration_flag";
        Query query = entityManager.createNativeQuery(sql)
                .unwrap(SQLQuery.class)
                .addScalar("title", StringType.INSTANCE)
                .addScalar("value", LongType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(DashboardChartItemDTO.class));
        return query.list();
    }

    public List<DashboardChartItemDTO> getIntegrationProjectPassItemList() {
        String sql = "SELECT IF ( t.count = t.successCount, '通过', '未通过' ) title, count(*) VALUE FROM ( SELECT count(b.id) count, SUM( IF ( b.id IS NULL OR b.last_run_state = 2, 0, 1 )) successCount FROM project a LEFT JOIN test_cases b ON a.id = b.project_id AND b.deleted = FALSE AND b.ci_flag = TRUE WHERE a.deleted = FALSE AND a.integration_flag = TRUE GROUP BY a.id ) t GROUP BY title";
        Query query = entityManager.createNativeQuery(sql)
                .unwrap(SQLQuery.class)
                .addScalar("title", StringType.INSTANCE)
                .addScalar("value", LongType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(DashboardChartItemDTO.class));
        return query.list();
    }

    public DashboardProjectSummaryDTO getProjectSummaryReport() {
        String sql = "SELECT count(t.id) projectCount, IFNULL(sum(t.integration_flag),0) projectIntegrationCount, IFNULL(sum( t.integration_flag * IF(t.ciCount = t.ciSuccessCount,1,0)),0) projectIntegrationPassCount, IFNULL(sum(t.integration_flag * t.count),0) integrationTestCasesCount, IFNULL(sum( t.integration_flag * t.ciCount ),0) integrationOnlineTestCasesCount, IFNULL(sum( t.integration_flag * t.ciSuccessCount),0) integrationOnlineTestCasesPassCount FROM ( SELECT a.id, a.`name`, a.integration_flag, SUM(IF(b.ci_flag = 1, 1, 0)) ciCount, count(b.id) count, SUM(IF(b.id is null or b.last_run_state = 2,0,1)) successCount,sum(IF(b.ci_flag = 1, 1, 0)*IF(b.id is null or b.last_run_state = 2,0,1)) ciSuccessCount FROM project a LEFT JOIN test_cases b ON a.id = b.project_id AND b.deleted = FALSE WHERE a.deleted = FALSE GROUP BY a.id ) t";
        Query query = entityManager.createNativeQuery(sql)
                .unwrap(SQLQuery.class)
                .addScalar("projectCount", LongType.INSTANCE)
                .addScalar("projectIntegrationCount", LongType.INSTANCE)
                .addScalar("projectIntegrationPassCount", LongType.INSTANCE)
                .addScalar("integrationTestCasesCount", LongType.INSTANCE)
                .addScalar("integrationOnlineTestCasesCount", LongType.INSTANCE)
                .addScalar("integrationOnlineTestCasesPassCount", LongType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(DashboardProjectSummaryDTO.class));
        return (DashboardProjectSummaryDTO) query.uniqueResult();
    }

    public int getProjectTotalCount() {
        String sql = "select count(*) from project where deleted = false";
        Query query = entityManager.createNativeQuery(sql)
                .unwrap(SQLQuery.class);
        return ((BigInteger) query.uniqueResult()).intValue();
    }

    public List<DashboardProjectDetailDTO> getProjectDetailList(Pagination pagination) {
        String sql = "SELECT a.id projectId, a.`name` projectName, a.integration_flag integrationFlag, count(b.id) testCasesCount, SUM(IF(b.id is null or b.last_run_state = 2, 0, 1)) testCasesPassCount, SUM(IF(b.ci_flag = 1, 1, 0)) onlineTestCasesCount, SUM( IF (b.id is null or b.last_run_state = 2, 0, 1) * IF (b.ci_flag = 1, 1, 0)) onlineTestCasesPassCount FROM project a LEFT JOIN test_cases b ON a.id = b.project_id AND b.deleted = FALSE WHERE a.deleted = FALSE GROUP BY a.id ORDER BY integrationFlag DESC, testCasesCount DESC, projectId DESC";
        Query query = entityManager.createNativeQuery(sql)
                .unwrap(SQLQuery.class)
                .addScalar("projectId", LongType.INSTANCE)
                .addScalar("projectName", StringType.INSTANCE)
                .addScalar("integrationFlag", BooleanType.INSTANCE)
                .addScalar("testCasesCount", LongType.INSTANCE)
                .addScalar("testCasesPassCount", LongType.INSTANCE)
                .addScalar("onlineTestCasesCount", LongType.INSTANCE)
                .addScalar("onlineTestCasesPassCount", LongType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(DashboardProjectDetailDTO.class))
                .setFirstResult(pagination.getStartRow())
                .setMaxResults(pagination.getPageSize());
        return query.list();
    }

    public List<DashboardChartItemDTO> getTestCasesPassItemList(DashboardTestCasesParams params) {
        String sql = "SELECT IF(a.last_run_state = 2, '失败数', '成功数' ) title, count(*) value FROM test_cases a left join project b ON a.project_id=b.id WHERE a.deleted=FALSE and b.deleted = FALSE ";
        if (params.getProjectId() != null) {
            sql += "AND a.project_id = :projectId ";
        }
        if (params.getIntegrationFlag() != null) {
            sql += "AND b.integration_flag = :integrationFlag ";
        }
        if (params.getCiFlag() != null) {
            sql += "AND a.ci_flag = :ciFlag ";
        }
        if (params.getLastRunState() != null) {
            sql += "AND a.last_run_state = :lastRunState ";
        }
        sql += " GROUP BY title";
        Query query = entityManager.createNativeQuery(sql)
                .unwrap(SQLQuery.class)
                .addScalar("title", StringType.INSTANCE)
                .addScalar("value", LongType.INSTANCE);
        if (params.getProjectId() != null) {
            query.setLong("projectId", params.getProjectId());
        }
        if (params.getIntegrationFlag() != null) {
            query.setBoolean("integrationFlag", params.getIntegrationFlag());
        }
        if (params.getCiFlag() != null) {
            query.setBoolean("ciFlag", params.getCiFlag());
        }
        if (params.getLastRunState() != null) {
            query.setInteger("lastRunState", params.getLastRunState());
        }
        query.setResultTransformer(Transformers.aliasToBean(DashboardChartItemDTO.class));
        return query.list();
    }

    public List<DashboardChartItemDTO> getTestCasesRunPassItemList(DashboardTestCasesParams params) {
        String sql = "SELECT IF (c.run_state=1, '通过数', '未通过数' ) title, count(*) VALUE " +
                "FROM test_cases a, project b, test_report_details c, test_report d " +
                "WHERE c.test_cases_id = a.id AND a.project_id = b.id AND c.report_uuid = d.report_uuid AND a.deleted = FALSE AND b.deleted = FALSE AND c.deleted = FALSE AND d.deleted = FALSE " +
                "AND d.start_time BETWEEN :startTime AND :endTime ";
        if (params.getTriggerMode() != null) {
            sql += "AND d.trigger_mode = :triggerMode ";
        }
        if (params.getProjectId() != null) {
            sql += "AND a.project_id = :projectId ";
        }
        if (params.getIntegrationFlag() != null) {
            sql += "AND b.integration_flag = :integrationFlag ";
        }
        if (params.getCiFlag() != null) {
            sql += "AND a.ci_flag = :ciFlag ";
        }
        if (params.getLastRunState() != null) {
            sql += "AND a.last_run_state = :lastRunState ";
        }
        sql += "GROUP BY title";
        Query query = entityManager.createNativeQuery(sql)
                .unwrap(SQLQuery.class)
                .addScalar("title", StringType.INSTANCE)
                .addScalar("value", LongType.INSTANCE)
                .setTimestamp("startTime", new Date(params.getStartTime()))
                .setTimestamp("endTime", new Date(params.getEndTime()));
        if (params.getTriggerMode() != null) {
            query.setInteger("triggerMode", params.getTriggerMode());
        }
        if (params.getProjectId() != null) {
            query.setLong("projectId", params.getProjectId());
        }
        if (params.getIntegrationFlag() != null) {
            query.setBoolean("integrationFlag", params.getIntegrationFlag());
        }
        if (params.getCiFlag() != null) {
            query.setBoolean("ciFlag", params.getCiFlag());
        }
        if (params.getLastRunState() != null) {
            query.setInteger("lastRunState", params.getLastRunState());
        }
        query.setResultTransformer(Transformers.aliasToBean(DashboardChartItemDTO.class));
        return query.list();
    }

    public DashboardTestCasesSummaryDTO getTestCasesSummaryReport(DashboardTestCasesParams params) {
        String sql = "SELECT count(*) testCasesCount, sum(IF(a.last_run_state = 1, 1, 0)) testCasesLastPassCount, sum(IF(a.last_run_state = 2, 1, 0)) testCasesLastFailCount, IFNULL(sum(t.count),0) testCasesRunCount, IFNULL(sum(t.successCount),0) testCasesRunPassCount, IFNULL(sum(t.failCount),0) testCasesRunFailCount " +
                "FROM test_cases a LEFT JOIN project b ON a.project_id = b.id AND b.deleted = FALSE " +
                "LEFT JOIN ( SELECT a.test_cases_id, count(*) count, sum(IF(a.run_state = 1, 1, 0)) successCount, sum(IF(a.run_state = 2, 1, 0)) failCount " +
                "FROM test_report_details a, test_report b WHERE a.report_uuid = b.report_uuid AND a.deleted = FALSE AND b.deleted = FALSE AND b.start_time BETWEEN :startTime AND :endTime ";
        if (params.getTriggerMode() != null) {
            sql += "AND b.trigger_mode = :triggerMode ";
        }
        sql += "GROUP BY a.test_cases_id ) t ON a.id = t.test_cases_id WHERE a.deleted = FALSE ";
        if (params.getIntegrationFlag() != null) {
            sql += "AND b.integration_flag = :integrationFlag ";
        }
        if (params.getCiFlag() != null) {
            sql += "AND a.ci_flag = :ciFlag ";
        }
        if (params.getProjectId() != null) {
            sql += "AND a.project_id = :projectId ";
        }
        if (params.getLastRunState() != null) {
            sql += "AND a.last_run_state = :lastRunState ";
        }
        Query query = entityManager.createNativeQuery(sql)
                .unwrap(SQLQuery.class)
                .addScalar("testCasesCount", LongType.INSTANCE)
                .addScalar("testCasesLastPassCount", LongType.INSTANCE)
                .addScalar("testCasesLastFailCount", LongType.INSTANCE)
                .addScalar("testCasesRunCount", LongType.INSTANCE)
                .addScalar("testCasesRunPassCount", LongType.INSTANCE)
                .addScalar("testCasesRunFailCount", LongType.INSTANCE)
                .setTimestamp("startTime", new Date(params.getStartTime()))
                .setTimestamp("endTime", new Date(params.getEndTime()));
        if (params.getTriggerMode() != null) {
            query.setInteger("triggerMode", params.getTriggerMode());
        }
        if (params.getProjectId() != null) {
            query.setLong("projectId", params.getProjectId());
        }
        if (params.getIntegrationFlag() != null) {
            query.setBoolean("integrationFlag", params.getIntegrationFlag());
        }
        if (params.getCiFlag() != null) {
            query.setBoolean("ciFlag", params.getCiFlag());
        }
        if (params.getLastRunState() != null) {
            query.setInteger("lastRunState", params.getLastRunState());
        }
        query.setResultTransformer(Transformers.aliasToBean(DashboardTestCasesSummaryDTO.class));
        return (DashboardTestCasesSummaryDTO) query.uniqueResult();
    }

    public int getTestCasesTotalCount(DashboardTestCasesParams params) {
        String sql = "SELECT count(*) FROM test_cases a LEFT JOIN project b ON a.project_id = b.id AND b.deleted = FALSE " +
                "LEFT JOIN ( SELECT a.test_cases_id FROM test_report_details a, test_report b WHERE a.report_uuid = b.report_uuid AND a.deleted = FALSE AND b.deleted = FALSE AND b.start_time BETWEEN :startTime AND :endTime ";
        if (params.getTriggerMode() != null) {
            sql += "AND b.trigger_mode = :triggerMode ";
        }
        sql += "GROUP BY a.test_cases_id ) t  ON a.id = t.test_cases_id WHERE a.deleted = FALSE ";
        if (params.getProjectId() != null) {
            sql += "AND a.project_id = :projectId ";
        }
        if (params.getIntegrationFlag() != null) {
            sql += "AND b.integration_flag = :integrationFlag ";
        }
        if (params.getCiFlag() != null) {
            sql += "AND a.ci_flag = :ciFlag ";
        }
        if (params.getLastRunState() != null) {
            sql += "AND a.last_run_state = :lastRunState ";
        }
        Query query = entityManager.createNativeQuery(sql)
                .unwrap(SQLQuery.class)
                .setTimestamp("startTime", new Date(params.getStartTime()))
                .setTimestamp("endTime", new Date(params.getEndTime()));
        if (params.getTriggerMode() != null) {
            query.setInteger("triggerMode", params.getTriggerMode());
        }
        if (params.getProjectId() != null) {
            query.setLong("projectId", params.getProjectId());
        }
        if (params.getIntegrationFlag() != null) {
            query.setBoolean("integrationFlag", params.getIntegrationFlag());
        }
        if (params.getCiFlag() != null) {
            query.setBoolean("ciFlag", params.getCiFlag());
        }
        if (params.getLastRunState() != null) {
            query.setInteger("lastRunState", params.getLastRunState());
        }
        return ((BigInteger) query.uniqueResult()).intValue();
    }

    public List<DashboardTestCasesDetailDTO> getTestCasesDetailList(DashboardTestCasesParams params, Pagination pagination) {
        String sql = "SELECT a.id testCasesId, a.`name` testCasesName, b.`name` projectName, b.integration_flag integrationFlag, a.ci_flag ciFlag, a.last_run_state lastRunState, IFNULL(sum(t.count), 0) runCount, IFNULL(sum(t.successCount), 0) runPassCount " +
                "FROM test_cases a LEFT JOIN project b ON a.project_id = b.id AND b.deleted = FALSE " +
                "LEFT JOIN ( SELECT a.test_cases_id, count(*) count, sum(IF(a.run_state = 1, 1, 0)) successCount FROM test_report_details a, test_report b WHERE a.report_uuid = b.report_uuid AND a.deleted = FALSE AND b.deleted = FALSE AND b.start_time BETWEEN :startTime AND :endTime ";
        if (params.getTriggerMode() != null) {
            sql += "AND b.trigger_mode = :triggerMode ";
        }
        sql += "GROUP BY a.test_cases_id ) t  ON a.id = t.test_cases_id WHERE a.deleted = FALSE ";
        if (params.getProjectId() != null) {
            sql += "AND a.project_id = :projectId ";
        }
        if (params.getIntegrationFlag() != null) {
            sql += "AND b.integration_flag = :integrationFlag ";
        }
        if (params.getCiFlag() != null) {
            sql += "AND a.ci_flag = :ciFlag ";
        }
        if (params.getLastRunState() != null) {
            sql += "AND a.last_run_state = :lastRunState ";
        }
        sql += "GROUP BY a.id ORDER BY integrationFlag DESC, ciFlag DESC, runCount DESC, testCasesId DESC ";
        Query query = entityManager.createNativeQuery(sql)
                .unwrap(SQLQuery.class)
                .addScalar("testCasesId", LongType.INSTANCE)
                .addScalar("testCasesName", StringType.INSTANCE)
                .addScalar("projectName", StringType.INSTANCE)
                .addScalar("integrationFlag", BooleanType.INSTANCE)
                .addScalar("ciFlag", BooleanType.INSTANCE)
                .addScalar("lastRunState", IntegerType.INSTANCE)
                .addScalar("runCount", LongType.INSTANCE)
                .addScalar("runPassCount", LongType.INSTANCE)
                .setTimestamp("startTime", new Date(params.getStartTime()))
                .setTimestamp("endTime", new Date(params.getEndTime()));
        if (params.getTriggerMode() != null) {
            query.setInteger("triggerMode", params.getTriggerMode());
        }
        if (params.getProjectId() != null) {
            query.setLong("projectId", params.getProjectId());
        }
        if (params.getIntegrationFlag() != null) {
            query.setBoolean("integrationFlag", params.getIntegrationFlag());
        }
        if (params.getCiFlag() != null) {
            query.setBoolean("ciFlag", params.getCiFlag());
        }
        if (params.getLastRunState() != null) {
            query.setInteger("lastRunState", params.getLastRunState());
        }
        query.setFirstResult(pagination.getStartRow())
                .setMaxResults(pagination.getPageSize())
                .setResultTransformer(Transformers.aliasToBean(DashboardTestCasesDetailDTO.class));
        return query.list();
    }

    public List<PlatformProjectTestReportDTO> getProjectTestReportListForQualityReport(DashboardProjectQualityReportParams params) {
        String sql = "SELECT rpt.id AS id, rpt.run_state AS runState, rpt.fail_reason_id AS failReasonId, rpt.fix_flag AS fixFlag, ( SELECT t2.project_id FROM test_report_details t1, test_cases t2 WHERE t1.report_uuid = rpt.report_uuid AND t1.test_cases_id = t2.id AND t1.deleted = FALSE LIMIT 1 ) projectId FROM test_report rpt WHERE rpt.deleted = FALSE AND rpt.trigger_mode IN (1, 4) AND rpt.start_time BETWEEN :startTime AND :endTime";
        Query query = entityManager.createNativeQuery(sql)
                .unwrap(SQLQuery.class)
                .addScalar("id", LongType.INSTANCE)
                .addScalar("runState", IntegerType.INSTANCE)
                .addScalar("failReasonId", IntegerType.INSTANCE)
                .addScalar("fixFlag", BooleanType.INSTANCE)
                .addScalar("projectId", LongType.INSTANCE)
                .setTimestamp("startTime", new Date(params.getStartTime()))
                .setTimestamp("endTime", new Date(params.getEndTime()))
                .setResultTransformer(Transformers.aliasToBean(PlatformProjectTestReportDTO.class));
        return query.list();
    }

    public List<String> getSelectDateRangeListForProjectQualityReportHistory() {
        String sql = "select DISTINCT date_range from project_quality_report_history order by date_range desc ";
        Query query = entityManager.createNativeQuery(sql)
                .unwrap(SQLQuery.class);
        return query.list();
    }

    public List<PlatformProjectQualityReportHistoryDTO> getProjectInfoListForQualityReport() {
        String sql = "SELECT pt.id id, pt.`name` projectName, pt.target_count apiTargetCount, pt.finish_count apiFinishCount, pt.test_owner_ids testOwnerIds, pt.dev_owner_ids devOwnerIds, pt.quality_score qualityScore, et.project_id projectId, count(et.id) onlineTestCasesCount, sum( CASE WHEN et.last_run_state = 1 THEN 1 ELSE 0 END ) onlineTestCasesSuccessCount FROM project pt, test_cases et WHERE et.project_id = pt.id AND pt.integration_flag = TRUE AND et.ci_Flag=TRUE AND et.deleted = FALSE AND pt.deleted = FALSE GROUP BY et.project_Id ORDER BY pt.`name` DESC";
        Query query = entityManager.createNativeQuery(sql)
                .unwrap(SQLQuery.class)
                .addScalar("id", LongType.INSTANCE)
                .addScalar("projectName", StringType.INSTANCE)
                .addScalar("apiTargetCount", LongType.INSTANCE)
                .addScalar("apiFinishCount", LongType.INSTANCE)
                .addScalar("testOwnerIds", StringType.INSTANCE)
                .addScalar("devOwnerIds", StringType.INSTANCE)
                .addScalar("qualityScore", DoubleType.INSTANCE)
                .addScalar("projectId", LongType.INSTANCE)
                .addScalar("onlineTestCasesCount", LongType.INSTANCE)
                .addScalar("onlineTestCasesSuccessCount", LongType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(PlatformProjectQualityReportHistoryDTO.class));
        return query.list();
    }

}