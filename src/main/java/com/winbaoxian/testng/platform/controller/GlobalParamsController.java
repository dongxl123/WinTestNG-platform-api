package com.winbaoxian.testng.platform.controller;

import com.winbaoxian.testng.platform.model.common.JsonResult;
import com.winbaoxian.testng.platform.model.common.Pagination;
import com.winbaoxian.testng.platform.model.common.PaginationDTO;
import com.winbaoxian.testng.platform.model.dto.IdParamDTO;
import com.winbaoxian.testng.platform.model.dto.PlatformGlobalParamsDTO;
import com.winbaoxian.testng.platform.service.GlobalParamsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 12:04
 */
@RestController
@RequestMapping(value = "/globalParams")
public class GlobalParamsController {

    @Resource
    private GlobalParamsService globalParamsService;

    /**
     * @api {POST} /globalParams/save 新增/更新全局参数
     * @apiVersion 1.0.0
     * @apiGroup GlobalParamsController
     * @apiName save
     * @apiParam (请求体) {Number} id 主键
     * @apiParam (请求体) {String} key 键
     * @apiParam (请求体) {String} value 值
     * @apiParam (请求体) {String} description 描述
     * @apiParamExample 请求体示例
     * {"description":"C6ywOa","id":3362,"value":"HYfw3fB","key":"3Z"}
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccessExample 响应结果示例
     * {"msg":"9","code":6250,"data":{}}
     */
    @PostMapping(value = "save")
    public JsonResult save(@RequestBody PlatformGlobalParamsDTO dto) {
        globalParamsService.save(dto);
        return JsonResult.createSuccessResult("操作成功");
    }

    /**
     * @api {GET} /globalParams/page 分页获取全局参数列表
     * @apiVersion 1.0.0
     * @apiGroup GlobalParamsController
     * @apiName page
     * @apiParam (请求参数) {String} key,支持模糊查询
     * @apiParam (请求参数) {Number} pageNum 第几页
     * @apiParam (请求参数) {Number} pageSize 每页数量
     * @apiParamExample 请求参数示例
     * pageSize=6833&pageNum=5236&key=OP8buA
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccess (响应结果) {Array} data.list
     * @apiSuccess (响应结果) {Number} data.list.id 主键
     * @apiSuccess (响应结果) {Number} data.list.createTime 创建时间
     * @apiSuccess (响应结果) {Number} data.list.updateTime 更新时间
     * @apiSuccess (响应结果) {String} data.list.key 键
     * @apiSuccess (响应结果) {String} data.list.value 值
     * @apiSuccess (响应结果) {String} data.list.description 描述
     * @apiSuccess (响应结果) {Number} data.pageNum 第几页
     * @apiSuccess (响应结果) {Number} data.pageSize 每页
     * @apiSuccess (响应结果) {Number} data.totalRow 总数
     * @apiSuccess (响应结果) {Number} data.totalPage 总页数
     * @apiSuccess (响应结果) {String} data.orderProperty 排序字段
     * @apiSuccess (响应结果) {String} data.orderDirection 排序方式, DESC 降序;ASC 升序
     * @apiSuccessExample 响应结果示例
     * {"msg":"fAwYswBOSt","code":2055,"data":{"totalRow":8260,"totalPage":4946,"pageSize":8296,"orderDirection":"vj0E","list":[{"createTime":827412274277,"description":"aM","updateTime":4048401796040,"id":1710,"value":"tkcRT","key":"0"}],"pageNum":2936,"orderProperty":"JrP4q9"}}
     */
    @GetMapping(value = "page")
    public JsonResult<PaginationDTO<PlatformGlobalParamsDTO>> page(String key, Pagination pagination) {
        return JsonResult.createSuccessResult(globalParamsService.page(key, pagination));
    }

    /**
     * @api {POST} /globalParams/delete 删除全局参数
     * @apiVersion 1.0.0
     * @apiGroup GlobalParamsController
     * @apiName delete
     * @apiParam (请求体) {Number} id 主键
     * @apiParamExample 请求体示例
     * {"id":4759}
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccessExample 响应结果示例
     * {"msg":"6t5fUdNq3q","code":4833,"data":{}}
     */
    @PostMapping(value = "delete")
    public JsonResult delete(@RequestBody IdParamDTO paramDTO) {
        globalParamsService.delete(paramDTO.getId());
        return JsonResult.createSuccessResult("操作成功");
    }


}
