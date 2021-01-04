package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class DashboardChartItemDTO implements Serializable {

    /**
     * 名称
     */
    private String title;
    /**
     * 值
     */
    private Long value;

}
