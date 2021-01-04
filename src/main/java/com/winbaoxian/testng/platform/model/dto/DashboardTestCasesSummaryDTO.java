package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class DashboardTestCasesSummaryDTO implements Serializable {

    /**
     * 测试任务总数
     */
    private Long testCasesCount;
    /**
     * 测试任务上次成功总数
     */
    private Long testCasesLastPassCount;
    /**
     * 测试任务上次失败总数
     */
    private Long testCasesLastFailCount;
    /**
     * 测试任务通过率cal
     */
    private Double testCasesPassRate;
    /**
     * 测试任务运行总次数
     */
    private Long testCasesRunCount;
    /**
     * 测试任务运行成功总次数
     */
    private Long testCasesRunPassCount;
    /**
     * 测试任务运行失败总次数
     */
    private Long testCasesRunFailCount;
    /**
     * 测试任务运行通过率cal
     */
    private Double testCasesRunPassRate;

}
