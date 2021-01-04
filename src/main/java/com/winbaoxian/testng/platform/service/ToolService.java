package com.winbaoxian.testng.platform.service;

import com.alibaba.fastjson.JSONObject;
import com.winbaoxian.testng.core.common.ParamsExecutor;
import com.winbaoxian.testng.platform.model.dto.ToolFreemarkerParams;
import com.winbaoxian.testng.platform.model.dto.ToolFreemarkerResultDTO;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ToolService {

    @Resource
    private ParamsExecutor paramsExecutor;

    public ToolFreemarkerResultDTO getFreemarkerResult(ToolFreemarkerParams.FreemarkerOperationEnum operationType, String tpl, JSONObject model) {
        ToolFreemarkerResultDTO resultDTO = new ToolFreemarkerResultDTO();
        String ret = null;
        if (ToolFreemarkerParams.FreemarkerOperationEnum.run.equals(operationType)) {
            ret = paramsExecutor.render(tpl, model);
        } else if (ToolFreemarkerParams.FreemarkerOperationEnum.escape.equals(operationType)) {
            ret = StringEscapeUtils.escapeJava(tpl);
        } else if (ToolFreemarkerParams.FreemarkerOperationEnum.unescape.equals(operationType)) {
            ret = StringEscapeUtils.unescapeJava(tpl);
        }
        resultDTO.setResult(ret);
        return resultDTO;
    }

}
