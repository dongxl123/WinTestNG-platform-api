package com.winbaoxian.testng.platform.controller;

import com.winbaoxian.testng.platform.model.common.JsonResult;
import com.winbaoxian.testng.platform.model.common.Pagination;
import com.winbaoxian.testng.platform.model.common.PaginationDTO;
import com.winbaoxian.testng.platform.model.dto.*;
import com.winbaoxian.testng.platform.service.TestCasesService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 11:56
 */
@RestController
@RequestMapping(value = "/testCases")
public class TestCasesController {

    @Resource
    private TestCasesService testCasesService;

    /**
     * @api {POST} /testCases/save 新增/修改测试任务
     * @apiVersion 1.0.0
     * @apiGroup TestCasesController
     * @apiName save
     * @apiParam (请求体) {Number} id 主键
     * @apiParam (请求体) {Number} ownerUid 责任人ID
     * @apiParam (请求体) {String} name 接口名称
     * @apiParam (请求体) {String} description 描述
     * @apiParam (请求体) {Number} projectId 所属项目
     * @apiParam (请求体) {Number} moduleId 模块
     * @apiParam (请求体) {Boolean} ciFlag 上线状态，true:已上线，false:未上线
     * @apiParam (请求体) {String} baseParams 基础数据,json格式
     * @apiParam (请求体) {Array} preActions 前置操作(可参数化)
     * @apiParam (请求体) {String} preActions.name 接口名称
     * @apiParam (请求体) {String} preActions.description 描述
     * @apiParam (请求体) {String} preActions.actionType HTTP,RESOURCE,SETV,ASSERTION,TPL
     * @apiParam (请求体) {String} preActions.settings 内容
     * @apiParam (请求体) {String} preActions.alias 返回结果定义的别名
     * @apiParam (请求体) {Number} preActions.delayTimes 延迟时间（单位毫秒）
     * @apiParam (请求体) {Boolean} preActions.disableFlag 是否禁用，true:禁用
     * @apiParam (请求体) {Object} dataPreparationConfig 数据准备配置(可参数化)
     * @apiParam (请求体) {String} dataPreparationConfig.type text,csv,resource
     * @apiParam (请求体) {String} dataPreparationConfig.showTitle 显示的标题，日志中用到，支持freemark
     * @apiParam (请求体) {String} dataPreparationConfig.fileName DataPreparationType.csv
     * @apiParam (请求体) {String} dataPreparationConfig.separator 分割符号
     * @apiParam (请求体) {Array} dataPreparationConfig.fieldNameList 字段名称列表
     * @apiParam (请求体) {String} dataPreparationConfig.text DataPreparationType.text
     * @apiParam (请求体) {Number} dataPreparationConfig.resourceId DataPreparationType.mysql
     * @apiParam (请求体) {String} dataPreparationConfig.sql 查询语句
     * @apiParam (请求体) {Array} mainActions 执行主流程操作(http、dubbo等，可参数化)
     * @apiParam (请求体) {String} mainActions.name 接口名称
     * @apiParam (请求体) {String} mainActions.description 描述
     * @apiParam (请求体) {String} mainActions.actionType HTTP,RESOURCE,SETV,ASSERTION,TPL
     * @apiParam (请求体) {String} mainActions.alias 返回结果定义的别名
     * @apiParam (请求体) {Number} mainActions.delayTimes 延迟时间（单位毫秒）
     * @apiParam (请求体) {Boolean} mainActions.disableFlag 是否禁用，true:禁用
     * @apiParam (请求体) {Array} postActions 后置操作
     * @apiParamExample 请求体示例
     * {"id":1,"creatorUid":1,"ownerUid":2,"name":"开发测试-调用template22","description":"没地方联待开发可大幅度发是的三阶魔方点开始链接付款垃圾筐代理费","projectId":2,"moduleId":1,"ciFlag":true,"preActions":[{"name":"查询MONGO数据库","description":null,"actionType":"RESOURCE","settings":"{\"resourceId\":2,\"sql\":\"db.dxl_products.findOne({}).skip(2).limit(1)\"}","alias":"mongo","delayTimes":2000},{"name":"设置值","description":null,"actionType":"SETV","settings":"{\"zz\":\"${mongo._id}\",\"m\":\"${mongo.item}\",\"n\":\"${mongo.qty}\"}","alias":"xxxx","delayTimes":null}],"dataPreparationConfig":null,"mainActions":[{"name":"调用登录模板","description":null,"actionType":"TPL","settings":"{\"mappings\":{\"a\":\"${mobile}\",\"b\":\"0000\"},\"templateId\":1}","alias":"template","delayTimes":2000}],"postActions":null}
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccessExample 响应结果示例
     * {"code":200,"msg":"操作成功","data":null}
     */
    @PostMapping(value = "save")
    public JsonResult save(@RequestBody PlatformTestCasesDTO dto) {
        testCasesService.save(dto);
        return JsonResult.createSuccessResult("操作成功");
    }

    /**
     * @api {GET} /testCases/page 分页查询测试任务列表
     * @apiVersion 1.0.0
     * @apiGroup TestCasesController
     * @apiName page
     * @apiParam (请求参数) {Number} ownerUid 责任人ID
     * @apiParam (请求参数) {String} name 接口名称,支持模糊查询
     * @apiParam (请求参数) {Number} projectId 所属项目
     * @apiParam (请求参数) {Number} moduleId 模块
     * @apiParam (请求参数) {Boolean} ciFlag 上线状态，true:已上线，false:未上线
     * @apiParam (请求参数) {Number} lastRunState 运行状态
     * @apiParam (请求参数) {Number} pageNum 第几页
     * @apiParam (请求参数) {Number} pageSize 每页数量
     * @apiParamExample 请求参数示例
     * lastRunState=3744&name=mG&pageSize=1646&ciFlag=false&ownerUid=9136&moduleId=6445&projectId=1010&pageNum=1075
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccess (响应结果) {Array} data.list 列表
     * @apiSuccess (响应结果) {String} data.list.projectName 项目名称
     * @apiSuccess (响应结果) {String} data.list.moduleName 模块名称
     * @apiSuccess (响应结果) {String} data.list.creatorName 创建人姓名
     * @apiSuccess (响应结果) {String} data.list.ownerName 责任人姓名
     * @apiSuccess (响应结果) {Number} data.list.id
     * @apiSuccess (响应结果) {Number} data.list.createTime 创建时间
     * @apiSuccess (响应结果) {Number} data.list.updateTime 更新时间
     * @apiSuccess (响应结果) {Number} data.list.creatorUid 创建人ID
     * @apiSuccess (响应结果) {Number} data.list.ownerUid 责任人ID
     * @apiSuccess (响应结果) {String} data.list.name 接口名称
     * @apiSuccess (响应结果) {String} data.list.description 描述
     * @apiSuccess (响应结果) {String} data.list.env 环境
     * @apiSuccess (响应结果) {Number} data.list.projectId 所属项目
     * @apiSuccess (响应结果) {Number} data.list.moduleId 模块
     * @apiSuccess (响应结果) {Boolean} data.list.ciFlag 上线状态，true:已上线，false:未上线
     * @apiSuccess (响应结果) {String} data.list.baseParams 基础数据
     * @apiSuccess (响应结果) {Array} data.list.preActions 前置操作(可参数化)
     * @apiSuccess (响应结果) {String} data.list.preActions.name 接口名称
     * @apiSuccess (响应结果) {String} data.list.preActions.description 描述
     * @apiSuccess (响应结果) {String} data.list.preActions.actionType HTTP,RESOURCE,SETV,ASSERTION,TPL
     * @apiSuccess (响应结果) {String} data.list.preActions.settings 内容
     * @apiSuccess (响应结果) {String} data.list.preActions.alias 返回结果定义的别名
     * @apiSuccess (响应结果) {Number} data.list.preActions.delayTimes 延迟时间（单位毫秒）
     * @apiSuccess (响应结果) {Boolean} data.list.preActions.disableFlag 是否禁用，true:禁用
     * @apiSuccess (响应结果) {Object} data.list.dataPreparationConfig 数据准备配置(可参数化)
     * @apiSuccess (响应结果) {String} data.list.dataPreparationConfig.type text,csv,resource
     * @apiSuccess (响应结果) {String} data.list.dataPreparationConfig.showTitle
     * @apiSuccess (响应结果) {String} data.list.dataPreparationConfig.fileName DataPreparationType.csv
     * @apiSuccess (响应结果) {String} data.list.dataPreparationConfig.separator
     * @apiSuccess (响应结果) {Array} data.list.dataPreparationConfig.fieldNameList
     * @apiSuccess (响应结果) {String} data.list.dataPreparationConfig.text DataPreparationType.text
     * @apiSuccess (响应结果) {Number} data.list.dataPreparationConfig.resourceId DataPreparationType.mysql
     * @apiSuccess (响应结果) {String} data.list.dataPreparationConfig.sql
     * @apiSuccess (响应结果) {Array} data.list.mainActions 执行主流程操作(http、dubbo等，可参数化)
     * @apiSuccess (响应结果) {String} data.list.mainActions.name 接口名称
     * @apiSuccess (响应结果) {String} data.list.mainActions.description 描述
     * @apiSuccess (响应结果) {String} data.list.mainActions.actionType HTTP,RESOURCE,SETV,ASSERTION,TPL
     * @apiSuccess (响应结果) {String} data.list.mainActions.settings 内容
     * @apiSuccess (响应结果) {String} data.list.mainActions.alias 返回结果定义的别名
     * @apiSuccess (响应结果) {Number} data.list.mainActions.delayTimes 延迟时间（单位毫秒）
     * @apiSuccess (响应结果) {Boolean} data.list.mainActions.disableFlag 是否禁用，true:禁用
     * @apiSuccess (响应结果) {Array} data.list.postActions 后置操作
     * @apiSuccess (响应结果) {String} data.list.postActions.name
     * @apiSuccess (响应结果) {String} data.list.postActions.description
     * @apiSuccess (响应结果) {String} data.list.postActions.actionType HTTP,RESOURCE,SETV,ASSERTION,TPL
     * @apiSuccess (响应结果) {String} data.list.postActions.settings
     * @apiSuccess (响应结果) {String} data.list.postActions.alias
     * @apiSuccess (响应结果) {Number} data.list.postActions.delayTimes
     * @apiSuccess (响应结果) {Boolean} data.list.postActions.disableFlag 是否禁用，true:禁用
     * @apiSuccess (响应结果) {Number} data.list.lastRunState 运行状态
     * @apiSuccess (响应结果) {Number} data.list.qualityScore
     * @apiSuccess (响应结果) {Boolean} data.list.deleted 是否删除， 1:删除， 0:有效
     * @apiSuccess (响应结果) {Number} data.pageNum 第几页
     * @apiSuccess (响应结果) {Number} data.pageSize 每页数量
     * @apiSuccess (响应结果) {Number} data.totalRow 总数
     * @apiSuccess (响应结果) {Number} data.totalPage 总页数
     * @apiSuccess (响应结果) {String} data.orderProperty 排序字段
     * @apiSuccess (响应结果) {String} data.orderDirection 排序方式, DESC 降序;ASC 升序
     * @apiSuccessExample 响应结果示例
     * {"msg":"0YPKdQ","code":7106,"data":{"totalRow":4094,"totalPage":1454,"pageSize":1005,"orderDirection":"V","list":[{"moduleName":"WiAwjrZ5","creatorName":"93","description":"HyBkJV","dataPreparationConfig":{"fileName":"xC8mJCQHOJ","resourceId":1234,"showTitle":"G7YKDAyfGh","text":"2XwaZ","type":"text","separator":"R","fieldNameList":["RfLuPwVH"],"sql":"JDFN1cxi4"},"updateTime":2631618798565,"ciFlag":false,"env":"B9iizc","preActions":[{"actionType":"TPL","settings":"3hDHbnA","delayTimes":5938,"name":"j2e","description":"fI68H","alias":"HYdAPd"}],"creatorUid":2215,"lastRunState":7069,"deleted":true,"ownerName":"TjptPEj","createTime":2209148097347,"name":"ZAU","postActions":[{"actionType":"HTTP","settings":"PkFaHH","delayTimes":5316,"name":"yli","description":"07RmrcxNx5","alias":"xeHijT"}],"id":4762,"mainActions":[{"actionType":"HTTP","settings":"t","delayTimes":8369,"name":"hz6I8zmI8F","description":"K9MACRHh9","alias":"J7kNR"}],"projectName":"JLWJ","ownerUid":6503,"moduleId":9976,"projectId":3696}],"pageNum":6632,"orderProperty":"4BOw"}}
     */
    @GetMapping(value = "page")
    public JsonResult<PaginationDTO<PlatformTestCasesDTO>> page(PlatformTestCasesDTO params, Pagination pagination) {
        return JsonResult.createSuccessResult(testCasesService.page(params, pagination));
    }

    /**
     * @api {POST} /testCases/delete 删除测试任务
     * @apiVersion 1.0.0
     * @apiGroup TestCasesController
     * @apiName delete
     * @apiParam (请求体) {Number} id 主键
     * @apiParamExample 请求体示例
     * {"id":4073}
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccessExample 响应结果示例
     * {"msg":"NzA","code":9853,"data":{}}
     */
    @PostMapping(value = "delete")
    public JsonResult delete(@RequestBody IdParamDTO paramDTO) {
        testCasesService.delete(paramDTO.getId());
        return JsonResult.createSuccessResult("操作成功");
    }

    /**
     * @api {POST} /testCases/run 执行测试任务
     * @apiVersion 1.0.0
     * @apiGroup TestCasesController
     * @apiName run
     * @apiParam (请求体) {Array} idList 测试任务ID列表
     * @apiParamExample 请求体示例
     * {"idList":[8208]}
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccess (响应结果) {String} data.uuid 唯一ID
     * @apiSuccessExample 响应结果示例
     * {"code":200,"msg":null,"data":{"uuid":"6a8a7901b2b64edc86a99a8bcbe0c664"}}
     */
    @PostMapping(value = "run")
    public JsonResult<UuidDTO> run(@RequestBody TestCasesRunParams params) {
        String uuid = testCasesService.run(params);
        UuidDTO dto = new UuidDTO();
        dto.setUuid(uuid);
        return JsonResult.createSuccessResult(dto);
    }

    /**
     * @api {GET} /testCases/getRunLog 获取运行日志
     * @apiVersion 1.0.0
     * @apiGroup TestCasesController
     * @apiName getRunLog
     * @apiParam (请求参数) {String} uuid 唯一UUID
     * @apiParam (请求参数) {Number} offset 日志偏移量，初始为0，传上次请求返回的值
     * @apiParamExample 请求参数示例
     * offset=2131&uuid=oqhnaxN1r
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccess (响应结果) {Number} data.offset 日志偏移量，初始为0
     * @apiSuccess (响应结果) {String} data.content 内容
     * @apiSuccess (响应结果) {Boolean} data.endFlag 是否执行完， true:已完成
     * @apiSuccess (响应结果) {String} data.reportUrl 测试报告地址
     * @apiSuccessExample 响应结果示例
     * {"msg":"wVyfyNgmg","code":550,"data":{"offset":6276,"endFlag":true,"reportUrl":"xm","content":"Nyi"}}
     */
    @GetMapping(value = "getRunLog")
    public JsonResult<ConsoleLogDTO> getRunLog(String uuid, Long offset) {
        ConsoleLogDTO dto = testCasesService.getRunLog(uuid, offset);
        return JsonResult.createSuccessResult(dto);
    }


    /**
     * @api {POST} /testCases/batchOnline 批量上线
     * @apiVersion 1.0.0
     * @apiGroup TestCasesController
     * @apiName batchOnline
     * @apiParam (请求体) {Array} idList 测试任务ID列表
     * @apiParamExample 请求体示例
     * {"idList":[3295]}
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccessExample 响应结果示例
     * {"msg":"o4S","code":177,"data":{}}
     */
    @PostMapping(value = "batchOnline")
    public JsonResult batchOnline(@RequestBody TestCasesRunParams params) {
        testCasesService.batchOnline(params);
        return JsonResult.createSuccessResult("操作成功");
    }

    /**
     * @api {POST} /testCases/batchOffline 批量下线
     * @apiVersion 1.0.0
     * @apiGroup TestCasesController
     * @apiName batchOffline
     * @apiParam (请求体) {Array} idList 测试任务ID列表
     * @apiParamExample 请求体示例
     * {"idList":[1016]}
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccessExample 响应结果示例
     * {"msg":"nuXd","code":5173,"data":{}}
     */
    @PostMapping(value = "batchOffline")
    public JsonResult batchOffline(@RequestBody TestCasesRunParams params) {
        testCasesService.batchOffline(params);
        return JsonResult.createSuccessResult("操作成功");
    }

}
