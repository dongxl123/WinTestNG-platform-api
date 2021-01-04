package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author dongxuanliang252
 * @date 2019-3-28 15:13:29
 */
@Setter
@Getter
public class TestCasesSelectDTO implements Serializable {

    private Long id;
    private String name;

}
