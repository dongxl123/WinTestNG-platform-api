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
public class AssertionTypeSelectDTO implements Serializable {

    private String value;
    private String title;

}
