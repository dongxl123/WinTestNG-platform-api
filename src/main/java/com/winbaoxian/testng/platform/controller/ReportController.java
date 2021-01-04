package com.winbaoxian.testng.platform.controller;

import com.winbaoxian.testng.platform.model.common.JsonResult;
import com.winbaoxian.testng.platform.model.common.Pagination;
import com.winbaoxian.testng.platform.model.common.PaginationDTO;
import com.winbaoxian.testng.platform.model.dto.PlatformTestReportDTO;
import com.winbaoxian.testng.platform.model.dto.ReportChooseFailReasonParams;
import com.winbaoxian.testng.platform.model.dto.ReportChooseFixFlagParams;
import com.winbaoxian.testng.platform.model.dto.TestReportQueryParams;
import com.winbaoxian.testng.platform.service.ReportService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 11:59
 */
@RestController
@RequestMapping(value = "/testReport")
public class ReportController {

    @Resource
    private ReportService reportService;

    /**
     * @api {GET} /testReport/page 分页获取测试报告
     * @apiVersion 1.0.0
     * @apiGroup ReportController
     * @apiName page
     * @apiParam (请求参数) {Number} projectId 项目ID
     * @apiParam (请求参数) {Number} moduleId 模块ID
     * @apiParam (请求参数) {Number} testCasesId 测试任务ID
     * @apiParam (请求参数) {Number} executorUid 执行人ID
     * @apiParam (请求参数) {Number} startTime 开始时间
     * @apiParam (请求参数) {Number} endTime 结束时间
     * @apiParam (请求参数) {Number} triggerMode 触发方式
     * @apiParam (请求参数) {Number} runState 运行状态
     * @apiParam (请求参数) {Number} pageNum 第几页
     * @apiParam (请求参数) {Number} pageSize 每页数量
     * @apiParamExample 请求参数示例
     * testCasesId=4873&executorUid=8060&pageSize=6565&startTime=3924&endTime=8106&moduleId=8174&projectId=2984&triggerMode=4540&runState=7431&pageNum=3557
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccess (响应结果) {Array} data.list 列表
     * @apiSuccess (响应结果) {String} data.list.executorName 执行人名字
     * @apiSuccess (响应结果) {String} data.list.reportUrl 测试报告地址
     * @apiSuccess (响应结果) {String} data.list.projectNames 项目名称
     * @apiSuccess (响应结果) {String} data.list.testCasesNames 测试任务名称
     * @apiSuccess (响应结果) {Number} data.list.id
     * @apiSuccess (响应结果) {Number} data.list.createTime 创建时间
     * @apiSuccess (响应结果) {Number} data.list.updateTime 更新时间
     * @apiSuccess (响应结果) {Number} data.list.executorUid 执行人UID
     * @apiSuccess (响应结果) {String} data.list.reportUuid 报告唯一UID
     * @apiSuccess (响应结果) {Number} data.list.startTime 开始时间
     * @apiSuccess (响应结果) {Number} data.list.endTime 结束时间
     * @apiSuccess (响应结果) {Number} data.list.duration 花费的时间(单位毫秒)
     * @apiSuccess (响应结果) {Number} data.list.totalCount 总数
     * @apiSuccess (响应结果) {Number} data.list.successCount 成功数
     * @apiSuccess (响应结果) {Number} data.list.failCount 失败数
     * @apiSuccess (响应结果) {Number} data.list.triggerMode 触发方式
     * @apiSuccess (响应结果) {Number} data.list.runState 运行状态
     * @apiSuccess (响应结果) {Number} data.list.failReasonId 错误原因
     * @apiSuccess (响应结果) {Boolean} data.list.fixFlag 是否修复 , true:已修复 false:未修复
     * @apiSuccess (响应结果) {Boolean} data.list.deleted 是否删除，1:删除， 0:有效
     * @apiSuccess (响应结果) {Number} data.pageNum 第几页
     * @apiSuccess (响应结果) {Number} data.pageSize 每页数量
     * @apiSuccess (响应结果) {Number} data.totalRow 总数
     * @apiSuccess (响应结果) {Number} data.totalPage 总页数
     * @apiSuccess (响应结果) {String} data.orderProperty 排序字段
     * @apiSuccess (响应结果) {String} data.orderDirection 排序方式, DESC 降序;ASC 升序
     * @apiSuccessExample 响应结果示例
     * {"msg":"n","code":5858,"data":{"totalRow":4704,"totalPage":1670,"pageSize":3395,"orderDirection":"8BUA7s9","list":[{"executorUid":4114,"failCount":8036,"fixFlag":true,"projectNames":"Ca0Y","updateTime":2885511712221,"reportUrl":"iVdGAMeCmW","totalCount":9316,"testCasesNames":"VM4","failReasonId":6723,"reportUuid":"W","duration":9551,"executorName":"5aswG0HT","deleted":false,"createTime":2656708761265,"successCount":8178,"startTime":4100532466142,"id":6701,"endTime":2107995827672,"triggerMode":436,"runState":3629}],"pageNum":8101,"orderProperty":"vn1n4d5G"}}
     */
    @GetMapping(value = "page")
    public JsonResult<PaginationDTO<PlatformTestReportDTO>> page(TestReportQueryParams params, Pagination pagination) {
        return JsonResult.createSuccessResult(reportService.page(params, pagination));
    }

    /**
     * @api {POST} /testReport/chooseFailReason 选择错误原因
     * @apiVersion 1.0.0
     * @apiGroup ReportController
     * @apiName chooseFailReason
     * @apiParam (请求体) {Number} id 主键
     * @apiParam (请求体) {Number} failReasonId 失败原因ID
     * @apiParamExample 请求体示例
     * {"id":539,"failReasonId":6171}
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccessExample 响应结果示例
     * {"msg":"ggK","code":8635,"data":{}}
     */
    @PostMapping(value = "chooseFailReason")
    public JsonResult chooseFailReason(@RequestBody ReportChooseFailReasonParams params) {
        reportService.chooseFailReason(params);
        return JsonResult.createSuccessResult("操作成功");
    }

    /**
     * @api {POST} /testReport/chooseFixFlag 选择修复状态
     * @apiVersion 1.0.0
     * @apiGroup ReportController
     * @apiName chooseFixFlag
     * @apiParam (请求体) {Number} id 主键
     * @apiParam (请求体) {Boolean} fixFlag 修复状态，true:已修复 false:未修复
     * @apiParamExample 请求体示例
     * {"fixFlag":true,"id":4388}
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccessExample 响应结果示例
     * {"msg":"h4PU","code":844,"data":{}}
     */
    @PostMapping(value = "chooseFixFlag")
    public JsonResult chooseFixFlag(@RequestBody ReportChooseFixFlagParams params) {
        reportService.chooseFixFlag(params);
        return JsonResult.createSuccessResult("操作成功");
    }

}
