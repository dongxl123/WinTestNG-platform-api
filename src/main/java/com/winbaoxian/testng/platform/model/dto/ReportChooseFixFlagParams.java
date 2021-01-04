package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ReportChooseFixFlagParams implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 修复状态，true:已修复 false:未修复
     */
    private Boolean fixFlag;

}
