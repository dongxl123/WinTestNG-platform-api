package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ReportChooseFailReasonParams implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     *失败原因ID
     */
    private  Integer failReasonId;

}
