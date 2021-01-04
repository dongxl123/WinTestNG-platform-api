package com.winbaoxian.testng.platform.controller;

import com.alibaba.fastjson.JSONObject;
import com.winbaoxian.common.freemarker.utils.JsonUtils;
import com.winbaoxian.testng.platform.exception.WinTestNgPlatformException;
import com.winbaoxian.testng.platform.model.common.JsonResult;
import com.winbaoxian.testng.platform.model.dto.ToolFreemarkerParams;
import com.winbaoxian.testng.platform.model.dto.ToolFreemarkerResultDTO;
import com.winbaoxian.testng.platform.service.ToolService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/tool")
public class ToolController {

    @Resource
    private ToolService toolService;

    /**
     * @api {POST} /tool/freemarker/getResult 获取freemarker表达式结果
     * @apiVersion 1.0.0
     * @apiGroup ToolController
     * @apiName getFreemarkerResult
     * @apiParam (请求体) {String} tpl freemarker表达式
     * @apiParam (请求体) {String} data 数据
     * @apiParamExample 请求体示例
     * {"tpl":"Ca4uOKayxH","data":"Gsa4cb9nHE"}
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccess (响应结果) {String} data.result 结果
     * @apiSuccessExample 响应结果示例
     * {"msg":"0YMgH","code":8709,"data":{"result":"OMvyfyAAu"}}
     */
    @PostMapping(value = "/freemarker/getResult")
    public JsonResult<ToolFreemarkerResultDTO> getFreemarkerResult(@RequestBody ToolFreemarkerParams params) {
        if (params == null) {
            throw new WinTestNgPlatformException("请求数据不能为空");
        }
        if (StringUtils.isBlank(params.getTpl())) {
            throw new WinTestNgPlatformException("请输入freemarker表达式");
        }
        JSONObject model = null;
        if (StringUtils.isNotBlank(params.getData())) {
            Object dataModel = JsonUtils.INSTANCE.parseObject(params.getData());
            if (dataModel instanceof JSONObject) {
                model = (JSONObject) dataModel;
            } else {
                throw new WinTestNgPlatformException("数据是非法JSON");
            }
        }
        ToolFreemarkerResultDTO result = toolService.getFreemarkerResult(params.getOperationType(), params.getTpl(), model);
        return JsonResult.createSuccessResult(result);
    }

}
