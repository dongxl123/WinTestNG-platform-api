package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2019-03-22 19:04
 */
@Getter
@Setter
public class ActionTemplateDebugParams implements Serializable {

    /**
     * 测试模板ID
     */
    private Long templateId;
    /**
     * 参数值映射
     */
    private Map<String, Object> mappings;

}
