package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author dongxuanliang252
 * @date 2019-04-02 10:23
 */
@Getter
@Setter
public class ConsoleLogDTO implements Serializable {

    /**
     * 日志偏移量，初始为0
     */
    private Long offset;
    /**
     * 内容
     */
    private String content;
    /**
     * 是否执行完， true:已完成
     */
    private Boolean endFlag;
    /**
     * 测试报告地址
     */
    private String reportUrl;

}
