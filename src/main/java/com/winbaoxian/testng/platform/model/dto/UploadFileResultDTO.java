package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author dongxuanliang252
 * @date 2019-04-01 13:55
 */
@Getter
@Setter
public class UploadFileResultDTO implements Serializable {

    /**
     * 文件路径
     */
    private String url;
}
