package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ReportFailReasonSelectDTO implements Serializable {

    /**
     * 值
     */
    private Integer value;
    /**
     * 名称
     */
    private String title;
    /**
     * 是否需要选择修复状态
     */
    private Boolean needFixFlag;

}