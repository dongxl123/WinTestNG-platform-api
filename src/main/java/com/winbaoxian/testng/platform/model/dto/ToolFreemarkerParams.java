package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ToolFreemarkerParams implements Serializable {

    /**
     * freemarker表达式
     */
    private String tpl;

    /**
     * 数据
     */
    private String data;

    /**
     * escape, unescape, run
     */
    private FreemarkerOperationEnum operationType;


    public enum FreemarkerOperationEnum {

        escape,
        unescape,
        run,
        ;
    }

}
