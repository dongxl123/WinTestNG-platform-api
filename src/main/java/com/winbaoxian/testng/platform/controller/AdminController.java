package com.winbaoxian.testng.platform.controller;

import com.winbaoxian.testng.platform.model.common.JsonResult;
import com.winbaoxian.testng.platform.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author dongxuanliang252
 * @date 2020-07-28 9:24
 */
@RestController
@RequestMapping(value = "admin")
@Slf4j
public class AdminController {

    @Resource
    private SchedulerService schedulerService;

    /**
     * @api {GET} /admin/analysisProjectApiInfo 管理操作-分析项目api接口
     * @apiVersion 1.0.0
     * @apiGroup AdminController
     * @apiName analysisProjectApiInfo
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccessExample 响应结果示例
     * {"msg":"IwF3c","code":5415,"data":{}}
     */
    @GetMapping(value = "analysisProjectApiInfo")
    public JsonResult analysisProjectApiInfo() {
        schedulerService.analysisProjectApiInfo();
        return JsonResult.createSuccessResult("操作成功");
    }

}
