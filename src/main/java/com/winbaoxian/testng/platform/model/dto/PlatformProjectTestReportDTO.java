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
public class PlatformProjectTestReportDTO extends TestReportDTO {

    /**
     * 项目ID
     */
    private Long projectId;

}
