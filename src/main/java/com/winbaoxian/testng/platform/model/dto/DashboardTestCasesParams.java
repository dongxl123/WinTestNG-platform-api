package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DashboardTestCasesParams implements Serializable {

    /**
     * 触发方式
     */
    private Integer triggerMode;
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 项目集成状态，true:已集成，false:未集成
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
     * 执行开始时间
     */
    private Long startTime;
    /**
     * 执行结束时间
     */
    private Long endTime;



}
