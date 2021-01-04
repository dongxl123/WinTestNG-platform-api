package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class DashboardChartDTO implements Serializable {

    /**
     * 图表名称
     */
    private String title;
    /**
     * 数据
     */
    private List<DashboardChartItemDTO> values;

}
