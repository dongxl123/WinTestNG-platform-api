package com.winbaoxian.testng.platform.model.dto;

import com.winbaoxian.testng.model.dto.TestReportDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dongxuanliang252
 * @date 2019-03-21 13:48
 */
@Setter
@Getter
public class PlatformTestReportDTO extends TestReportDTO {

    /**
     * 执行人名字
     */
    private String executorName;

    /**
     * 测试报告地址
     */
    private String reportUrl;

    /**
     * 项目名称
     */
    private String projectNames;

    /**
     * 测试任务名称
     */
    private String testCasesNames;
}
