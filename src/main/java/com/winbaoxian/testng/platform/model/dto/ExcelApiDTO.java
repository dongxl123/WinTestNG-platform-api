package com.winbaoxian.testng.platform.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@HeadFontStyle(fontHeightInPoints = 11, bold = false)
public class ExcelApiDTO implements Serializable {

    /**
     * 序号
     */
    @ExcelProperty(value = "主键")
    @ColumnWidth(5)
    private Long id;
    /**
     * 接口地址
     */
    @ExcelProperty(value = "接口地址")
    @ColumnWidth(40)
    private String apiUrl;
    /**
     * 接口标题
     */
    @ColumnWidth(40)
    @ExcelProperty(value = "接口标题")
    private String apiTitle;
    /**
     * 目标标记
     */
    @ExcelProperty(value = "目标标记")
    private String targetFlag;
    /**
     * 完成状态
     */
    @ExcelProperty(value = "完成状态")
    private String finishFlag;

}
