package com.winbaoxian.testng.platform.controller;

import com.winbaoxian.testng.platform.model.common.JsonResult;
import com.winbaoxian.testng.platform.model.common.Pagination;
import com.winbaoxian.testng.platform.model.common.PaginationDTO;
import com.winbaoxian.testng.platform.model.dto.*;
import com.winbaoxian.testng.platform.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/dashboard")
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    /**
     * @api {GET} /dashboard/project/getChartList 项目-获取图表数据
     * @apiVersion 1.0.0
     * @apiGroup DashboardController
     * @apiName getProjectChartList
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Array} data 数据
     * @apiSuccess (响应结果) {String} data.title 图表名称
     * @apiSuccess (响应结果) {Array} data.values 数据
     * @apiSuccess (响应结果) {String} data.values.title 名称
     * @apiSuccess (响应结果) {Number} data.values.value 值
     * @apiSuccessExample 响应结果示例
     * {"code":200,"msg":null,"data":[{"title":"项目集成率","values":[{"title":"未集成","value":19},{"title":"已集成","value":6}]},{"title":"已集成项目通过率","values":[{"title":"未通过","value":6},{"title":"通过","value":16}]}]}
     */
    @GetMapping(value = "/project/getChartList")
    public JsonResult<List<DashboardChartDTO>> getProjectChartList() {
        List<DashboardChartDTO> dtoList = new ArrayList<>();
        DashboardChartDTO integrationChartDTO = dashboardService.getProjectIntegrationRateChart();
        dtoList.add(integrationChartDTO);
        DashboardChartDTO passChartDTO = dashboardService.getIntegrationProjectPassRateChart();
        dtoList.add(passChartDTO);
        return JsonResult.createSuccessResult(dtoList);
    }

    /**
     * @api {GET} /dashboard/project/getSummaryReport 项目-获取汇总数据
     * @apiVersion 1.0.0
     * @apiGroup DashboardController
     * @apiName getProjectSummaryReport
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccess (响应结果) {Number} data.projectCount 项目总数
     * @apiSuccess (响应结果) {Number} data.projectIntegrationCount 项目集成数
     * @apiSuccess (响应结果) {Number} data.projectIntegrationRate 项目集成率cal
     * @apiSuccess (响应结果) {Number} data.projectIntegrationPassCount 已集成项目通过数
     * @apiSuccess (响应结果) {Number} data.projectIntegrationPassRate 已集成项目通过率cal
     * @apiSuccess (响应结果) {Number} data.integrationTestCasesCount 测试任务总数（已集成）
     * @apiSuccess (响应结果) {Number} data.integrationOnlineTestCasesCount 测试任务总数（已集成已上线）
     * @apiSuccess (响应结果) {Number} data.integrationOfflineTestCasesCount 测试任务总数（已集成未上线）cal
     * @apiSuccess (响应结果) {Number} data.integrationOnlineTestCasesPassCount 测试任务成功数（已集成已上线）
     * @apiSuccess (响应结果) {Number} data.integrationOnlineTestCasesPassRate 测试任务通过率（已集成已上线）cal
     * @apiSuccessExample 响应结果示例
     * {"msg":"G","code":1110,"data":{"projectCount":7482,"integrationOfflineTestCasesCount":638,"projectIntegrationCount":7671,"integrationOnlineTestCasesPassCount":1367,"projectIntegrationRate":8143.94,"integrationOnlineTestCasesCount":4960,"integrationTestCasesCount":1521,"projectIntegrationPassRate":1561.01,"integrationOnlineTestCasesPassRate":5997.86,"projectIntegrationPassCount":2468}}
     */
    @GetMapping(value = "/project/getSummaryReport")
    public JsonResult<DashboardProjectSummaryDTO> getProjectSummaryReport() {
        DashboardProjectSummaryDTO summaryDTO = dashboardService.getProjectSummaryReport();
        return JsonResult.createSuccessResult(summaryDTO);
    }

    /**
     * @api {GET} /dashboard/project/getDetailPage 项目-分页获取项目报表明细数据
     * @apiVersion 1.0.0
     * @apiGroup DashboardController
     * @apiName getProjectDetailPage
     * @apiParam (请求参数) {Number} pageNum 第几页
     * @apiParam (请求参数) {Number} pageSize 每页数量
     * @apiParam (请求参数) {Number} totalRow 总数
     * @apiParam (请求参数) {Number} totalPage 总页数
     * @apiParam (请求参数) {String} orderProperty 排序字段
     * @apiParam (请求参数) {String} orderDirection 排序方式, DESC 降序;ASC 升序
     * @apiParamExample 请求参数示例
     * totalRow=1610&totalPage=9457&pageSize=2711&orderDirection=MHqovha&pageNum=2476&orderProperty=Rh
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccess (响应结果) {Array} data.list 列表
     * @apiSuccess (响应结果) {Number} data.list.projectId 项目ID
     * @apiSuccess (响应结果) {String} data.list.projectName 项目名称
     * @apiSuccess (响应结果) {Boolean} data.list.integrationFlag 集成状态，true:已集成，false:未集成
     * @apiSuccess (响应结果) {Number} data.list.testCasesCount 测试任务数
     * @apiSuccess (响应结果) {Number} data.list.testCasesPassCount 测试任务成功数
     * @apiSuccess (响应结果) {Number} data.list.testCasesFailCount 测试任务失败数cal
     * @apiSuccess (响应结果) {Number} data.list.testCasesPassRate 测试任务通过率cal
     * @apiSuccess (响应结果) {Number} data.list.onlineTestCasesCount 上线测试任务数
     * @apiSuccess (响应结果) {Number} data.list.offlineTestCasesCount 未上线测试任务数cal
     * @apiSuccess (响应结果) {Number} data.list.onlineTestCasesPassCount 上线测试任务成功数
     * @apiSuccess (响应结果) {Number} data.list.onlineTestCasesFailCount 上线测试任务失败数cal
     * @apiSuccess (响应结果) {Number} data.list.onlineTestCasesPassRate 上线测试任务通过率cal
     * @apiSuccess (响应结果) {Number} data.pageNum 第几页
     * @apiSuccess (响应结果) {Number} data.pageSize 每页数量
     * @apiSuccess (响应结果) {Number} data.totalRow 总数
     * @apiSuccess (响应结果) {Number} data.totalPage 总页数
     * @apiSuccess (响应结果) {String} data.orderProperty 排序字段
     * @apiSuccess (响应结果) {String} data.orderDirection 排序方式, DESC 降序;ASC 升序
     * @apiSuccessExample 响应结果示例
     * {"msg":"E5","code":9074,"data":{"totalRow":2788,"totalPage":2415,"pageSize":6206,"orderDirection":"U","list":[{"offlineTestCasesCount":1246,"onlineTestCasesPassRate":7061.51,"onlineTestCasesCount":8082,"testCasesPassRate":9855.48,"integrationFlag":true,"testCasesPassCount":4132,"onlineTestCasesPassCount":6364,"testCasesCount":8967,"onlineTestCasesFailCount":7561,"projectName":"Cb7QLk","projectId":2153,"testCasesFailCount":5291}],"pageNum":1971,"orderProperty":"e7LO3GZnZd"}}
     */
    @GetMapping(value = "/project/getDetailPage")
    public JsonResult<PaginationDTO<DashboardProjectDetailDTO>> getProjectDetailPage(Pagination pagination) {
        PaginationDTO<DashboardProjectDetailDTO> page = dashboardService.getProjectDetailPage(pagination);
        return JsonResult.createSuccessResult(page);
    }

    /**
     * @api {GET} /dashboard/testCases/getChartList 测试任务-获取图表数据
     * @apiVersion 1.0.0
     * @apiGroup DashboardController
     * @apiName getTestCasesChartList
     * @apiParam (请求参数) {Number} triggerMode 触发方式
     * @apiParam (请求参数) {Number} projectId 项目ID
     * @apiParam (请求参数) {Boolean} integrationFlag 项目集成状态，true:已集成，false:未集成
     * @apiParam (请求参数) {Boolean} ciFlag 上线状态，true:已上线，false:未上线
     * @apiParam (请求参数) {Number} lastRunState 上次运行状态
     * @apiParam (请求参数) {Number} startTime 执行开始时间
     * @apiParam (请求参数) {Number} endTime 执行结束时间
     * @apiParamExample 请求参数示例
     * lastRunState=3877&integrationFlag=true&ciFlag=false&startTime=3159&endTime=9683&triggerMode=8543&projectId=8925
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Array} data 数据
     * @apiSuccess (响应结果) {String} data.title 图表名称
     * @apiSuccess (响应结果) {Array} data.values 数据
     * @apiSuccess (响应结果) {String} data.values.title 名称
     * @apiSuccess (响应结果) {Number} data.values.value 值
     * @apiSuccessExample 响应结果示例
     * {"code":200,"msg":null,"data":[{"title":"测试任务当前通过率","values":[{"title":"失败数","value":12},{"title":"成功数","value":163}]},{"title":"运行通过率","values":[{"title":"未通过数","value":38},{"title":"通过数","value":145}]}]}
     */
    @GetMapping(value = "/testCases/getChartList")
    public JsonResult<List<DashboardChartDTO>> getTestCasesChartList(DashboardTestCasesParams params) {
        List<DashboardChartDTO> dtoList = new ArrayList<>();
        DashboardChartDTO passRateChartDTO = dashboardService.getTestCasesPassRateChart(params);
        dtoList.add(passRateChartDTO);
        DashboardChartDTO runPassChartDTO = dashboardService.getTestCasesRunPassRateChart(params);
        dtoList.add(runPassChartDTO);
        return JsonResult.createSuccessResult(dtoList);
    }

    /**
     * @api {GET} /dashboard/testCases/getSummaryReport 测试任务-获取汇总数据
     * @apiVersion 1.0.0
     * @apiGroup DashboardController
     * @apiName getTestCasesSummaryReport
     * @apiParam (请求参数) {Number} triggerMode 触发方式
     * @apiParam (请求参数) {Number} projectId 项目ID
     * @apiParam (请求参数) {Boolean} integrationFlag 项目集成状态，true:已集成，false:未集成
     * @apiParam (请求参数) {Boolean} ciFlag 上线状态，true:已上线，false:未上线
     * @apiParam (请求参数) {Number} lastRunState 上次运行状态
     * @apiParam (请求参数) {Number} startTime 执行开始时间
     * @apiParam (请求参数) {Number} endTime 执行结束时间
     * @apiParamExample 请求参数示例
     * lastRunState=1821&integrationFlag=false&ciFlag=true&startTime=4647&endTime=1153&triggerMode=9491&projectId=8379
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccess (响应结果) {Number} data.testCasesCount 测试任务总数
     * @apiSuccess (响应结果) {Number} data.testCasesLastPassCount 测试任务上次成功总数
     * @apiSuccess (响应结果) {Number} data.testCasesLastFailCount 测试任务上次失败总数
     * @apiSuccess (响应结果) {Number} data.testCasesPassRate 测试任务通过率
     * @apiSuccess (响应结果) {Number} data.testCasesRunCount 测试任务运行总次数
     * @apiSuccess (响应结果) {Number} data.testCasesRunPassCount 测试任务运行成功总次数
     * @apiSuccess (响应结果) {Number} data.testCasesRunFailCount 测试任务运行失败总次数
     * @apiSuccess (响应结果) {Number} data.testCasesRunPassRate 测试任务运行通过率
     * @apiSuccessExample 响应结果示例
     * {"msg":"HXLOT0qMG","code":3871,"data":{"testCasesRunFailCount":1529,"testCasesRunCount":5354,"testCasesPassRate":5787.45,"testCasesRunPassCount":1881,"testCasesLastFailCount":9856,"testCasesRunPassRate":6199.35,"testCasesCount":9303,"testCasesLastPassCount":4665}}
     */
    @GetMapping(value = "/testCases/getSummaryReport")
    public JsonResult<DashboardTestCasesSummaryDTO> getTestCasesSummaryReport(DashboardTestCasesParams params) {
        DashboardTestCasesSummaryDTO summaryDTO = dashboardService.getTestCasesSummaryReport(params);
        return JsonResult.createSuccessResult(summaryDTO);
    }

    /**
     * @api {GET} /dashboard/testCases/getDetailPage 测试任务-分页获取测试任务报表明细数据
     * @apiVersion 1.0.0
     * @apiGroup DashboardController
     * @apiName getTestCasesDetailPage
     * @apiParam (请求参数) {Number} triggerMode 触发方式
     * @apiParam (请求参数) {Number} projectId 项目ID
     * @apiParam (请求参数) {Boolean} integrationFlag 项目集成状态，true:已集成，false:未集成
     * @apiParam (请求参数) {Boolean} ciFlag 上线状态，true:已上线，false:未上线
     * @apiParam (请求参数) {Number} lastRunState 上次运行状态
     * @apiParam (请求参数) {Number} startTime 执行开始时间
     * @apiParam (请求参数) {Number} endTime 执行结束时间
     * @apiParam (请求参数) {Number} pageNum 第几页
     * @apiParam (请求参数) {Number} pageSize 每页数量
     * @apiParam (请求参数) {Number} totalRow 总数
     * @apiParam (请求参数) {Number} totalPage 总页数
     * @apiParam (请求参数) {String} orderProperty 排序字段
     * @apiParam (请求参数) {String} orderDirection 排序方式, DESC 降序;ASC 升序
     * @apiParamExample 请求参数示例
     * totalRow=60&totalPage=7976&pageSize=1570&ciFlag=false&pageNum=8790&lastRunState=3718&integrationFlag=false&orderDirection=EtDb&startTime=2649&endTime=789&triggerMode=9686&projectId=2200&orderProperty=hQv1bv7nkb
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccess (响应结果) {Array} data.list 列表
     * @apiSuccess (响应结果) {Number} data.list.testCasesId 测试任务ID
     * @apiSuccess (响应结果) {String} data.list.testCasesName 测试任务名称
     * @apiSuccess (响应结果) {String} data.list.projectName 项目名称
     * @apiSuccess (响应结果) {Boolean} data.list.integrationFlag 集成状态，true:已集成，false:未集成
     * @apiSuccess (响应结果) {Boolean} data.list.ciFlag 上线状态，true:已上线，false:未上线
     * @apiSuccess (响应结果) {Number} data.list.lastRunState 上次运行状态
     * @apiSuccess (响应结果) {Number} data.list.runCount 运行总次数
     * @apiSuccess (响应结果) {Number} data.list.runPassCount 运行成功次数
     * @apiSuccess (响应结果) {Number} data.list.runPassRate 运行通过率
     * @apiSuccess (响应结果) {Number} data.pageNum 第几页
     * @apiSuccess (响应结果) {Number} data.pageSize 每页数量
     * @apiSuccess (响应结果) {Number} data.totalRow 总数
     * @apiSuccess (响应结果) {Number} data.totalPage 总页数
     * @apiSuccess (响应结果) {String} data.orderProperty 排序字段
     * @apiSuccess (响应结果) {String} data.orderDirection 排序方式, DESC 降序;ASC 升序
     * @apiSuccessExample 响应结果示例
     * {"msg":"Vc","code":8422,"data":{"totalRow":7458,"totalPage":118,"pageSize":28,"orderDirection":"3Tfc","list":[{"testCasesId":9775,"runPassCount":6254,"lastRunState":3379,"integrationFlag":false,"ciFlag":true,"projectName":"XTpXjZiuEZ","runCount":1556,"testCasesName":"De5GsKFYGl","runPassRate":5553.95}],"pageNum":8209,"orderProperty":"8hgR"}}
     */
    @GetMapping(value = "/testCases/getDetailPage")
    public JsonResult<PaginationDTO<DashboardTestCasesDetailDTO>> getTestCasesDetailPage(DashboardTestCasesParams params, Pagination pagination) {
        PaginationDTO<DashboardTestCasesDetailDTO> page = dashboardService.getTestCasesDetailPage(params, pagination);
        return JsonResult.createSuccessResult(page);
    }

    /**
     * @api {GET} /dashboard/projectQualityReport/getReport 当前项目质量报告-获取报告列表
     * @apiVersion 1.0.0
     * @apiGroup DashboardController
     * @apiName getProjectQualityReport
     * @apiParam (请求参数) {Number} startTime 执行开始时间
     * @apiParam (请求参数) {Number} endTime 执行结束时间
     * @apiParamExample 请求参数示例
     * startTime=2879&endTime=9999
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Array} data 数据
     * @apiSuccess (响应结果) {String} data.projectName 项目名称
     * @apiSuccess (响应结果) {Number} data.runFailCount 执行失败次数
     * @apiSuccess (响应结果) {Array} data.testOwnerList 对应测试数据，展示使用
     * @apiSuccess (响应结果) {Number} data.testOwnerList.id 主键
     * @apiSuccess (响应结果) {String} data.testOwnerList.name 名称
     * @apiSuccess (响应结果) {Array} data.devOwnerList 对应开发数据，展示使用
     * @apiSuccess (响应结果) {Number} data.devOwnerList.id 主键
     * @apiSuccess (响应结果) {String} data.devOwnerList.name 名称
     * @apiSuccess (响应结果) {Number} data.apiFinishRate 接口完成进度
     * @apiSuccess (响应结果) {Number} data.onlineTestCasesPassRate 上线测试任务通过率
     * @apiSuccess (响应结果) {Array} data.failInfoList 失败原因列表，展示使用
     * @apiSuccess (响应结果) {String} data.failInfoList.failReason 失败原因
     * @apiSuccess (响应结果) {Boolean} data.failInfoList.needFixFlag 是否需要修复标识
     * @apiSuccess (响应结果) {Number} data.failInfoList.failNum 失败数
     * @apiSuccess (响应结果) {Number} data.failInfoList.fixNum 已修复数
     * @apiSuccess (响应结果) {Number} data.id 主键
     * @apiSuccess (响应结果) {Number} data.projectId 项目ID
     * @apiSuccess (响应结果) {Number} data.runTotalCount 执行次数
     * @apiSuccess (响应结果) {Number} data.runSuccessCount 执行成功次数
     * @apiSuccess (响应结果) {String} data.runFailInfo 失败原因及修复情况
     * @apiSuccess (响应结果) {Number} data.apiTargetCount 接口目标数
     * @apiSuccess (响应结果) {Number} data.apiFinishCount 接口完成数据
     * @apiSuccess (响应结果) {Number} data.qualityScore 项目质量分
     * @apiSuccess (响应结果) {Number} data.onlineTestCasesCount 上线测试任务数
     * @apiSuccess (响应结果) {Number} data.onlineTestCasesSuccessCount 上线测试任务成功数
     * @apiSuccess (响应结果) {Boolean} data.deleted
     * @apiSuccessExample 响应结果示例
     * {"msg":"YmE0U2wi","code":7590,"data":[{"devOwnerList":[{"name":"t7Ti6","id":9679}],"testOwnerList":[{"name":"a1jn","id":7228}],"apiFinishCount":6003,"runFailCount":8085,"onlineTestCasesPassRate":7190.31,"apiTargetCount":3030,"apiFinishRate":5938.54,"onlineTestCasesCount":4417,"deleted":false,"failInfoList":[{"failNum":6071,"failReason":"eUF","needFixFlag":false,"fixNum":2580}],"runSuccessCount":9891,"runFailInfo":"SbdMGwCs","qualityScore":210.69,"projectName":"mvP","projectId":9038,"runTotalCount":2423,"onlineTestCasesSuccessCount":7271}]}
     */
    @GetMapping(value = "/projectQualityReport/getReport")
    public JsonResult<List<PlatformProjectQualityReportHistoryDTO>> getProjectQualityReport(DashboardProjectQualityReportParams params) {
        List<PlatformProjectQualityReportHistoryDTO> report = dashboardService.getProjectQualityReportForDashboard(params);
        return JsonResult.createSuccessResult(report);
    }

    /**
     * @api {GET} /dashboard/projectQualityReportHistory/getSelectDateRangeList 历史项目质量报告-查询时间选择列表
     * @apiVersion 1.0.0
     * @apiGroup DashboardController
     * @apiName getSelectDateRangeListForProjectQualityReportHistory
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Array} data 数据
     * @apiSuccessExample 响应结果示例
     * {"msg":"Y46Z","code":6923,"data":["uAw0YR"]}
     */
    @GetMapping(value = "/projectQualityReportHistory/getSelectDateRangeList")
    public JsonResult<List<String>> getSelectDateRangeListForProjectQualityReportHistory() {
        List<String> dateRangeList = dashboardService.getSelectDateRangeListForProjectQualityReportHistory();
        return JsonResult.createSuccessResult(dateRangeList);
    }

    /**
     * @api {GET} /dashboard/projectQualityReportHistory/getReport 历史项目质量报告-获取报告列表
     * @apiVersion 1.0.0
     * @apiGroup DashboardController
     * @apiName getProjectQualityReportHistory
     * @apiParam (请求参数) {String} dateRange
     * @apiParamExample 请求参数示例
     * dateRange=I9ajte3v
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Array} data 数据
     * @apiSuccess (响应结果) {String} data.projectName 项目名称
     * @apiSuccess (响应结果) {Number} data.runFailCount 执行失败次数
     * @apiSuccess (响应结果) {Array} data.testOwnerList 对应测试数据，展示使用
     * @apiSuccess (响应结果) {Number} data.testOwnerList.id 主键
     * @apiSuccess (响应结果) {String} data.testOwnerList.name 名称
     * @apiSuccess (响应结果) {Array} data.devOwnerList 对应开发数据，展示使用
     * @apiSuccess (响应结果) {Number} data.devOwnerList.id 主键
     * @apiSuccess (响应结果) {String} data.devOwnerList.name 名称
     * @apiSuccess (响应结果) {Number} data.apiFinishRate 接口完成进度
     * @apiSuccess (响应结果) {Number} data.onlineTestCasesPassRate 上线测试任务通过率
     * @apiSuccess (响应结果) {Array} data.failInfoList 失败原因列表，展示使用
     * @apiSuccess (响应结果) {String} data.failInfoList.failReason 失败原因
     * @apiSuccess (响应结果) {Boolean} data.failInfoList.needFixFlag 是否需要修复标识
     * @apiSuccess (响应结果) {Number} data.failInfoList.failNum 失败数
     * @apiSuccess (响应结果) {Number} data.failInfoList.fixNum 已修复数
     * @apiSuccess (响应结果) {Number} data.id
     * @apiSuccess (响应结果) {Number} data.createTime
     * @apiSuccess (响应结果) {Number} data.updateTime
     * @apiSuccess (响应结果) {Number} data.startTime
     * @apiSuccess (响应结果) {Number} data.endTime
     * @apiSuccess (响应结果) {String} data.dateRange
     * @apiSuccess (响应结果) {Number} data.projectId
     * @apiSuccess (响应结果) {String} data.testOwnerIds
     * @apiSuccess (响应结果) {String} data.devOwnerIds
     * @apiSuccess (响应结果) {Number} data.runTotalCount
     * @apiSuccess (响应结果) {Number} data.runSuccessCount
     * @apiSuccess (响应结果) {String} data.runFailInfo
     * @apiSuccess (响应结果) {Number} data.apiTargetCount
     * @apiSuccess (响应结果) {Number} data.apiFinishCount
     * @apiSuccess (响应结果) {Number} data.qualityScore
     * @apiSuccess (响应结果) {Number} data.onlineTestCasesCount
     * @apiSuccess (响应结果) {Number} data.onlineTestCasesSuccessCount
     * @apiSuccess (响应结果) {Boolean} data.deleted
     * @apiSuccessExample 响应结果示例
     * {"msg":"PkGScpnu6k","code":1597,"data":[{"testOwnerIds":"4tR","dateRange":"vtP4J6","testOwnerList":[{"name":"rS20yEud","id":3151}],"runFailCount":4862,"apiTargetCount":4492,"apiFinishRate":1187.1,"qualityScore":5228.5,"startTime":288407674539,"id":1539,"runTotalCount":5260,"devOwnerList":[{"name":"2SOa4Q","id":9795}],"updateTime":2090949521145,"devOwnerIds":"ewJLl","apiFinishCount":6983,"onlineTestCasesPassRate":4634.67,"onlineTestCasesCount":7910,"deleted":true,"failInfoList":[{"failNum":97,"failReason":"SnL","needFixFlag":false,"fixNum":2603}],"createTime":2505069935294,"runSuccessCount":3236,"runFailInfo":"uDL4lj3wkd","endTime":3883370864742,"projectName":"tFaZAvevNv","projectId":1084,"onlineTestCasesSuccessCount":5287}]}
     */
    @GetMapping(value = "/projectQualityReportHistory/getReport")
    public JsonResult<List<PlatformProjectQualityReportHistoryDTO>> getProjectQualityReportHistory(String dateRange) {
        List<PlatformProjectQualityReportHistoryDTO> report = dashboardService.getProjectQualityReportHistory(dateRange);
        return JsonResult.createSuccessResult(report);
    }

}


