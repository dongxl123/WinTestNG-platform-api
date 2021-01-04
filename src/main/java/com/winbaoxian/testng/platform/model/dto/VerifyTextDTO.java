package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author dongxuanliang252
 * @date 2019-03-25 15:03
 */
@Getter
@Setter
public class VerifyTextDTO implements Serializable {

    /**
     * 类型，action 操作;
     */
    private String type;
    /**
     * type的下的子分类，当type=action时，subType传actionType(HTTP、RESOURCE..)
     */
    private String subType;
    /**
     * 校验的字段名字，如 sql 查询语句,requestHeader 请求头
     */
    private String fieldName;
    /**
     * 需要校验的文本
     */
    private String text;

}
