package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author dongxuanliang252
 * @date 2019-03-28 10:34
 */
@Setter
@Getter
public class TestReportQueryParams implements Serializable {
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 模块ID
     */
    private Long moduleId;
    /**
     * 测试任务ID
     */
    private Long testCasesId;
    /**
     * 执行人ID
     */
    private Long executorUid;
    /**
     * 开始时间
     */
    private Long startTime;
    /**
     * 结束时间
     */
    private Long endTime;
    /**
     * 触发方式
     */
    private Integer triggerMode;
    /**
     * 运行状态
     */
    private Integer runState;
}
