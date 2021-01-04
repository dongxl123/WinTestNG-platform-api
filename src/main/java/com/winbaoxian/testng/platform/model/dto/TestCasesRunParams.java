package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2019-03-22 19:04
 */
@Getter
@Setter
public class TestCasesRunParams implements Serializable {

    /**
     * 测试任务ID列表
     */
    private List<Long> idList;

}
