package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class DashboardTestCasesDetailDTO implements Serializable {

    /**
     * 测试任务ID
     */
    private Long testCasesId;
    /**
     * 测试任务名称
     */
    private String testCasesName;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 集成状态，true:已集成，false:未集成
     */
    private Boolean integrationFlag;
    /**
     * 上线状态，true:已上线，false:未上线
     */
    private Boolean ciFlag;
    /**
     * 上次运行状态
     */
    private Integer lastRunState;

    /**
     * 运行总次数
     */
    private Long runCount;
    /**
     * 运行成功次数
     */
    private Long runPassCount;
    /**
     * 运行通过率
     */
    private Double runPassRate;

}
