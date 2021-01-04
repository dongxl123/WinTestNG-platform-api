package com.winbaoxian.testng.platform.model.dto;

import com.winbaoxian.testng.model.dto.TestReportDetailsDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dongxuanliang252
 * @date 2019-03-21 13:48
 */
@Setter
@Getter
public class PlatformTestReportDetailsDTO extends TestReportDetailsDTO {

    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 模块名称
     */
    private String moduleName;
    /**
     * 责任人姓名
     */
    private String ownerName;
    /**
     * 测试用例名称
     */
    private String testCasesName;
    private boolean hasExceptions;
}
