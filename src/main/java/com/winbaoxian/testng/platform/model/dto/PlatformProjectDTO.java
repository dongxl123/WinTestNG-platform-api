package com.winbaoxian.testng.platform.model.dto;

import com.winbaoxian.testng.model.dto.ProjectDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-03-21 13:48
 */
@Setter
@Getter
public class PlatformProjectDTO extends ProjectDTO {

    /**
     * 接口完成进度
     */
    private Double finishRate;

    /**
     * 对应测试数据，展示使用
     */
    private List<IdNameDTO> testOwnerList;

    /**
     * 对应开发数据，展示使用
     */
    private List<IdNameDTO> devOwnerList;

}
