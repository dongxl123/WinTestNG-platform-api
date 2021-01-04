package com.winbaoxian.testng.platform.controller;

import com.winbaoxian.testng.platform.model.common.JsonResult;
import com.winbaoxian.testng.platform.model.dto.IdParamDTO;
import com.winbaoxian.testng.platform.model.dto.PlatformModuleDTO;
import com.winbaoxian.testng.platform.service.ModuleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 11:56
 */
@RestController
@RequestMapping(value = "/module")
public class ModuleController {

    @Resource
    private ModuleService moduleService;

    /**
     * @api {POST} /module/save 新增/更新模块
     * @apiVersion 1.0.0
     * @apiGroup ModuleController
     * @apiName save
     * @apiParam (请求体) {Number} id 主键
     * @apiParam (请求体) {String} name 名称
     * @apiParam (请求体) {Number} projectId 项目ID
     * @apiParam (请求体) {String} description 描述
     * @apiParamExample 请求体示例
     * {"name":"M2","description":"w1ZM","id":5840,"projectId":3892}
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccessExample 响应结果示例
     * {"msg":"cp5kgB2","code":605,"data":{}}
     */
    @PostMapping(value = "save")
    public JsonResult save(@RequestBody PlatformModuleDTO dto) {
        moduleService.save(dto);
        return JsonResult.createSuccessResult("操作成功");
    }

    /**
     * @api {GET} /module/list 获取模块列表
     * @apiVersion 1.0.0
     * @apiGroup ModuleController
     * @apiName list
     * @apiParam (请求参数) {Number} projectId 项目ID
     * @apiParamExample 请求参数示例
     * projectId=6092
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Array} data 数据
     * @apiSuccess (响应结果) {Number} data.id 主键
     * @apiSuccess (响应结果) {Number} data.createTime 创建时间
     * @apiSuccess (响应结果) {Number} data.updateTime 更新时间
     * @apiSuccess (响应结果) {String} data.name 名称
     * @apiSuccess (响应结果) {Number} data.projectId 项目ID
     * @apiSuccess (响应结果) {String} data.description 描述
     * @apiSuccessExample 响应结果示例
     * {"msg":"PkmAut","code":2048,"data":[{"createTime":2200863326474,"name":"xvsANp","description":"XmO2rQ","updateTime":3532691389243,"id":4908,"projectId":4889}]}
     */
    @GetMapping(value = "list")
    public JsonResult<List<PlatformModuleDTO>> list(Long projectId) {
        return JsonResult.createSuccessResult(moduleService.list(projectId));
    }

    /**
     * @api {POST} /module/delete 删除模块
     * @apiVersion 1.0.0
     * @apiGroup ModuleController
     * @apiName delete
     * @apiParam (请求体) {Number} id 主键
     * @apiParamExample 请求体示例
     * {"id":7693}
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccessExample 响应结果示例
     * {"msg":"RlGDC7o","code":9106,"data":{}}
     */
    @PostMapping(value = "delete")
    public JsonResult delete(@RequestBody IdParamDTO paramDTO) {
        moduleService.delete(paramDTO.getId());
        return JsonResult.createSuccessResult("操作成功");
    }
}
