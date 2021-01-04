package com.winbaoxian.testng.platform.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class IdCount12DTO implements Serializable {

    public IdCount12DTO() {
    }

    public IdCount12DTO(Long id, Long count1, Long count2) {
        this.id = id;
        this.count1 = count1;
        this.count2 = count2;
    }

    /**
     * 主键
     */
    private Long id;

    /**
     * 数量
     */
    private Long count1;

    /**
     * 数量
     */
    private Long count2;

}
