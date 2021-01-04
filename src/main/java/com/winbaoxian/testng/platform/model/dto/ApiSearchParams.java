package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class ApiSearchParams implements Serializable {

    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 目标标识，true：是 ; false：否
     */
    private Boolean targetFlag;
    /**
     * 是否已完成，true：已完成 ; false：未完成
     */
    private Boolean finishFlag;
    /**
     * 搜索词，支持模糊搜索
     */
    private String searchKey;

}
