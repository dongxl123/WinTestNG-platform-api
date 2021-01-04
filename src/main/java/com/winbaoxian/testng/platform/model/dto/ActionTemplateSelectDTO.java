package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class ActionTemplateSelectDTO implements Serializable {

    private Long id;
    private String name;
    private String[] params;
}
