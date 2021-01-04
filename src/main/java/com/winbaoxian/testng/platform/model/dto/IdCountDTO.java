package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class IdCountDTO implements Serializable {

    public IdCountDTO() {
    }

    public IdCountDTO(Long id, Long count) {
        this.id = id;
        this.count = count;
    }

    /**
     * 主键
     */
    private Long id;

    /**
     * 数量
     */
    private Long count;

}
