package com.winbaoxian.testng.platform.model.dto;

import com.winbaoxian.testng.enums.ResourceType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author dongxuanliang252
 * @date 2019-03-21 18:00
 */
@Setter
@Getter
public class ResourceConfigSelectDTO implements Serializable {

    private Long id;
    private String name;
    private ResourceType resourceType;

}
