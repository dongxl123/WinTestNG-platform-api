package com.winbaoxian.testng.platform.model.dto;

import com.winbaoxian.testng.model.dto.ProjectQualityReportHistoryDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2020年6月24日16:56:39
 */
@Setter
@Getter
public class PlatformProjectQualityReportHistoryDTO extends ProjectQualityReportHistoryDTO {

    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 执行失败次数
     */
    private Long runFailCount;
    /**
     * 对应测试数据，展示使用
     */
    private List<IdNameDTO> testOwnerList;

    /**
     * 对应开发数据，展示使用
     */
    private List<IdNameDTO> devOwnerList;
    /**
     * 接口完成进度
     */
    private Double apiFinishRate;

    /**
     * 上线测试任务通过率
     */
    private Double onlineTestCasesPassRate;

    /**
     * 失败原因列表，展示使用
     */
    private List<RunFailInfoDTO> failInfoList;

    @Setter
    @Getter
    public static class RunFailInfoDTO implements Serializable{

        /**
         * 失败原因
         */
        private String failReason;
        /**
         * 是否需要修复标识
         */
        private Boolean needFixFlag;
        /**
         * 失败数
         */
        private Long failNum;
        /**
         * 已修复数
         */
        private Long fixNum;

    }

}
