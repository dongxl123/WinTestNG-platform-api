package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 项目总数
 * 项目集成数
 * 项目集成率
 * 已集成项目通过数
 * 已集成项目通过率
 * 测试任务总数（已集成）
 * 测试任务总数（已集成已上线）
 * 测试任务总数（已集成未上线）
 * 测试任务成功数（已集成已上线）
 * 测试任务失败数（已集成已上线）
 * 测试任务通过率（已集成已上线）
 */
@Setter
@Getter
public class DashboardProjectSummaryDTO implements Serializable {
    /**
     * 项目总数
     */
    private Long projectCount;
    /**
     * 项目集成数
     */
    private Long projectIntegrationCount;
    /**
     * 项目集成率cal
     */
    private Double projectIntegrationRate;
    /**
     * 已集成项目通过数
     */
    private Long projectIntegrationPassCount;
    /**
     * 已集成项目通过率cal
     */
    private Double projectIntegrationPassRate;
    /**
     * 测试任务总数（已集成）
     */
    private Long integrationTestCasesCount;
    /**
     * 测试任务总数（已集成已上线）
     */
    private Long integrationOnlineTestCasesCount;
    /**
     * 测试任务总数（已集成未上线）cal
     */
    private Long integrationOfflineTestCasesCount;
    /**
     * 测试任务成功数（已集成已上线）
     */
    private Long integrationOnlineTestCasesPassCount;
    /**
     * 测试任务通过率（已集成已上线）cal
     */
    private Double integrationOnlineTestCasesPassRate;

}
