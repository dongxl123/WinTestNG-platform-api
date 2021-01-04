package com.winbaoxian.testng.platform.controller;

import com.winbaoxian.testng.platform.model.common.JsonResult;
import com.winbaoxian.testng.platform.model.common.Pagination;
import com.winbaoxian.testng.platform.model.common.PaginationDTO;
import com.winbaoxian.testng.platform.model.dto.IdParamDTO;
import com.winbaoxian.testng.platform.model.dto.PlatformProjectDTO;
import com.winbaoxian.testng.platform.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author dongxuanliang252
 * @date 2019-03-19 17:37
 */
@RestController
@RequestMapping(value = "/project")
public class ProjectController {

    @Resource
    private ProjectService projectService;

    /**
     * @api {POST} /project/save 新增/修改项目信息
     * @apiVersion 1.0.0
     * @apiGroup ProjectController
     * @apiName save
     * @apiParam (请求体) {Number} id 主键
     * @apiParam (请求体) {String} name 名称
     * @apiParam (请求体) {Boolean} integrationFlag 集成状态，true：是 ; false：否
     * @apiParam (请求体) {String} description 描述
     * @apiParam (请求体) {String} mailAddress 配置项目邮件发送地址
     * @apiParam (请求体) {Number} targetCount 目标数量
     * @apiParam (请求体) {Number} finishCount 完成数量
     * @apiParam (请求体) {String} testOwnerIds 测试负责人ID，多个用，隔开
     * @apiParam (请求体) {String} devOwnerIds 开发负责人ID，多个用，隔开
     * @apiParam (请求体) {Number} qualityScore 质量分
     * @apiParam (请求体) {String} gitAddress git仓库地址
     * @apiParamExample 请求体示例
     * {"testOwnerIds":"wZr","gitAddress":"s","integrationFlag":false,"finishCount":4240,"name":"EBB","qualityScore":8289.99,"description":"6hLwNKT","mailAddress":"vw6F","devOwnerNames":"CrRnsqNBuN","id":8655,"targetCount":6759}
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccessExample 响应结果示例
     * {"msg":"hAR9H65Pqv","code":4997,"data":{}}
     */
    @PostMapping(value = "save")
    public JsonResult save(@RequestBody PlatformProjectDTO dto) {
        projectService.save(dto);
        return JsonResult.createSuccessResult("操作成功");
    }


    /**
     * @api {GET} /project/page 分页获取项目列表
     * @apiVersion 1.0.0
     * @apiGroup ProjectController
     * @apiName page
     * @apiParam (请求参数) {String} name 名称，支持模糊查询
     * @apiParam (请求参数) {Boolean} integrationFlag 集成状态，true：是 ; false：否
     * @apiParam (请求参数) {Number} pageNum 第几页
     * @apiParam (请求参数) {Number} pageSize 每页数量
     * @apiParamExample 请求参数示例
     * integrationFlag=false&name=l3&pageSize=3811&pageNum=4122
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccess (响应结果) {Array} data.list 列表
     * @apiSuccess (响应结果) {Number} data.list.finishRate 接口完成进度
     * @apiSuccess (响应结果) {Array} data.list.testOwnerList 对应测试数据，展示使用
     * @apiSuccess (响应结果) {Number} data.list.testOwnerList.id 主键
     * @apiSuccess (响应结果) {String} data.list.testOwnerList.name 名称
     * @apiSuccess (响应结果) {Array} data.list.devOwnerList 对应开发数据，展示使用
     * @apiSuccess (响应结果) {Number} data.list.devOwnerList.id 主键
     * @apiSuccess (响应结果) {String} data.list.devOwnerList.name 名称
     * @apiSuccess (响应结果) {Number} data.list.id 主键
     * @apiSuccess (响应结果) {Number} data.list.createTime 创建时间
     * @apiSuccess (响应结果) {Number} data.list.updateTime 更新时间
     * @apiSuccess (响应结果) {String} data.list.name 名称
     * @apiSuccess (响应结果) {Boolean} data.list.integrationFlag 集成状态，true：是 ; false：否
     * @apiSuccess (响应结果) {String} data.list.description 描述
     * @apiSuccess (响应结果) {String} data.list.mailAddress 配置项目邮件发送地址
     * @apiSuccess (响应结果) {Number} data.list.targetCount 目标数量
     * @apiSuccess (响应结果) {Number} data.list.finishCount 完成数量
     * @apiSuccess (响应结果) {String} data.list.testOwnerIds 测试负责人ID，多个用，隔开
     * @apiSuccess (响应结果) {String} data.list.devOwnerIds 开发负责人ID，多个用，隔开
     * @apiSuccess (响应结果) {Number} data.list.qualityScore 质量分
     * @apiSuccess (响应结果) {String} data.list.gitAddress git仓库地址
     * @apiSuccess (响应结果) {Number} data.pageNum 第几页
     * @apiSuccess (响应结果) {Number} data.pageSize 每页数量
     * @apiSuccess (响应结果) {Number} data.totalRow 总数
     * @apiSuccess (响应结果) {Number} data.totalPage 总页数
     * @apiSuccess (响应结果) {String} data.orderProperty 排序字段
     * @apiSuccess (响应结果) {String} data.orderDirection 排序方式, DESC 降序;ASC 升序
     * @apiSuccessExample 响应结果示例
     * {"msg":"JmvoIKF7tW","code":7150,"data":{"totalRow":7897,"totalPage":673,"pageSize":1414,"orderDirection":"x2yS6hv45","list":[{"testOwnerIds":"CKbHqSU","devOwnerList":[{"name":"3zoC8xq9W","id":1950}],"description":"cR","mailAddress":"j4yAfXs9M","updateTime":3957242865071,"testOwnerList":[{"name":"CmVfEk","id":882}],"devOwnerIds":"xhApZXLtB","targetCount":3058,"finishRate":6272.85,"gitAddress":"r608DGoz","createTime":4025566380642,"integrationFlag":true,"finishCount":1214,"name":"t","qualityScore":7115.8,"id":11}],"pageNum":9797,"orderProperty":"WWHsLqOtf"}}
     */
    @GetMapping(value = "page")
    public JsonResult<PaginationDTO<PlatformProjectDTO>> page(String name, Boolean integrationFlag, Pagination pagination) {
        return JsonResult.createSuccessResult(projectService.page(name, integrationFlag, pagination));
    }

    /**
     * @api {POST} /project/delete 删除项目
     * @apiVersion 1.0.0
     * @apiGroup ProjectController
     * @apiName delete
     * @apiParam (请求体) {Number} id 主键
     * @apiParamExample 请求体示例
     * {"id":6971}
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccessExample 响应结果示例
     * {"msg":"操作成功","code":200,"data":{}}
     */
    @PostMapping(value = "delete")
    public JsonResult delete(@RequestBody IdParamDTO paramDTO) {
        projectService.delete(paramDTO.getId());
        return JsonResult.createSuccessResult("操作成功");
    }

}
