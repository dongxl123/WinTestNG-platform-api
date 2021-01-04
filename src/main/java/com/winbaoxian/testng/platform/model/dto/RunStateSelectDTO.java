package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author dongxuanliang252
 * @date 2019-12-24 18:00
 */
@Setter
@Getter
public class RunStateSelectDTO implements Serializable {

    private Integer value;
    private String title;

}
