package com.winbaoxian.testng.platform.model.dto;

import com.winbaoxian.testng.model.dto.TestCasesDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dongxuanliang252
 * @date 2019-03-22 11:27
 */
@Getter
@Setter
public class PlatformTestCasesDTO extends TestCasesDTO {

    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 模块名称
     */
    private String moduleName;
    /**
     * 创建人姓名
     */
    private String creatorName;
    /**
     * 责任人姓名
     */
    private String ownerName;

}
