package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DashboardProjectQualityReportParams implements Serializable {

    /**
     * 执行开始时间
     */
    private Long startTime;
    /**
     * 执行结束时间
     */
    private Long endTime;


}
