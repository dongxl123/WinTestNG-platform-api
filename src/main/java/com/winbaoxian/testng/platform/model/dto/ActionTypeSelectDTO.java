package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author dongxuanliang252
 * @date 2019-03-21 18:00
 */
@Setter
@Getter
public class ActionTypeSelectDTO implements Serializable {

    private String value;
    private String title;
    /**
     * 逻辑组件标识，逻辑组件：含有子步骤，无别名alias
     */
    private Boolean logicFlag;

}
