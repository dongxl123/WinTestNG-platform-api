package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class DashboardProjectDetailDTO implements Serializable {

    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 集成状态，true:已集成，false:未集成
     */
    private Boolean integrationFlag;
    /**
     * 测试任务数
     */
    private Long testCasesCount;
    /**
     * 测试任务成功数
     */
    private Long testCasesPassCount;
    /**
     * 测试任务失败数cal
     */
    private Long testCasesFailCount;
    /**
     * 测试任务通过率cal
     */
    private Double testCasesPassRate;
    /**
     * 上线测试任务数
     */
    private Long onlineTestCasesCount;
    /**
     * 未上线测试任务数cal
     */
    private Long offlineTestCasesCount;
    /**
     * 上线测试任务成功数
     */
    private Long onlineTestCasesPassCount;
    /**
     * 上线测试任务失败数cal
     */
    private Long onlineTestCasesFailCount;
    /**
     * 上线测试任务通过率cal
     */
    private Double onlineTestCasesPassRate;

}
