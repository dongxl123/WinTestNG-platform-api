package com.winbaoxian.testng.platform.controller;

import com.winbaoxian.testng.enums.RequestMethod;
import com.winbaoxian.testng.enums.*;
import com.winbaoxian.testng.platform.model.common.JsonResult;
import com.winbaoxian.testng.platform.model.dto.*;
import com.winbaoxian.testng.platform.service.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dongxuanliang252
 */
@RestController
@RequestMapping(value = "/common")
public class CommonController {

    @Resource
    private CommonService commonService;
    @Resource
    private ProjectService projectService;
    @Resource
    private ModuleService moduleService;
    @Resource
    private TestCasesService testCasesService;
    @Resource
    private ResourceConfigService resourceConfigService;
    @Resource
    private ActionTemplateService actionTemplateService;
    @Resource
    private UserService userService;

    /**
     * @api {GET} /common/getResourceTypeList 获取资源类型列表
     * @apiVersion 1.0.0
     * @apiGroup CommonController
     * @apiName getResourceTypeList
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Array} data 数据
     * @apiSuccessExample 响应结果示例
     * {"code":200,"msg":null,"data":["mysql","mongo","redis","mq"]}
     */
    @GetMapping(value = "getResourceTypeList")
    public JsonResult<ResourceType[]> getResourceTypeList() {
        ResourceType[] resourceTypes = ResourceType.values();
        return JsonResult.createSuccessResult(resourceTypes);
    }


    /**
     * @api {GET} /common/getActionTypeList 获取操作类型列表
     * @apiVersion 1.0.0
     * @apiGroup CommonController
     * @apiName getActionTypeList
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Array} data 数据
     * @apiSuccess (响应结果) {String} data.value 值, 用于提交数据
     * @apiSuccess (响应结果) {String} data.title 显示文案
     * @apiSuccessExample 响应结果示例
     * {"code":200,"msg":null,"data":[{"value":"HTTP","title":"HTTP请求","logicFlag":false},{"value":"RESOURCE","title":"资源操作","logicFlag":false},{"value":"SETV","title":"常量设置","logicFlag":false},{"value":"ASSERTION","title":"断言","logicFlag":false},{"value":"TPL","title":"调用模板","logicFlag":false},{"value":"GROUP","title":"分组","logicFlag":true},{"value":"IF","title":"IF判断","logicFlag":true},{"value":"FOR","title":"FOR循环","logicFlag":true}]}
     */
    @GetMapping(value = "getActionTypeList")
    public JsonResult<List<ActionTypeSelectDTO>> getActionTypeList() {
        List<ActionTypeSelectDTO> list = new ArrayList<>();
        for (ActionType actionType : ActionType.values()) {
            ActionTypeSelectDTO selectDTO = new ActionTypeSelectDTO();
            selectDTO.setValue(actionType.name());
            selectDTO.setTitle(actionType.getTitle());
            selectDTO.setLogicFlag(actionType.getLogicFlag());
            list.add(selectDTO);
        }
        return JsonResult.createSuccessResult(list);
    }

    /**
     * @api {GET} /common/getRunStateList 获取执行状态下拉列表
     * @apiVersion 1.0.0
     * @apiGroup CommonController
     * @apiName getRunStateList
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Array} data 数据
     * @apiSuccess (响应结果) {Number} data.value 值
     * @apiSuccess (响应结果) {String} data.title 名称
     * @apiSuccessExample 响应结果示例
     * {"msg":"yNOWJtmblb","code":7179,"data":[{"title":"m","value":313}]}
     */
    @GetMapping(value = "getRunStateList")
    public JsonResult<List<RunStateSelectDTO>> getRunStateList() {
        List<RunStateSelectDTO> list = new ArrayList<>();
        for (RunState runState : RunState.values()) {
            RunStateSelectDTO selectDTO = new RunStateSelectDTO();
            selectDTO.setValue(runState.getValue());
            selectDTO.setTitle(runState.getTitle());
            list.add(selectDTO);
        }
        return JsonResult.createSuccessResult(list);
    }

    /**
     * @api {GET} /common/getTriggerModeList 获取触发方式下拉列表
     * @apiVersion 1.0.0
     * @apiGroup CommonController
     * @apiName getTriggerModeList
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Array} data 数据
     * @apiSuccess (响应结果) {Number} data.value 值
     * @apiSuccess (响应结果) {String} data.title 名称
     * @apiSuccessExample 响应结果示例
     * {"msg":"fvebq6","code":8843,"data":[{"title":"Sb5caAdUZ","value":9837}]}
     */
    @GetMapping(value = "getTriggerModeList")
    public JsonResult<List<TriggerModeSelectDTO>> getTriggerModeList() {
        List<TriggerModeSelectDTO> list = new ArrayList<>();
        for (TriggerMode triggerMode : TriggerMode.values()) {
            TriggerModeSelectDTO selectDTO = new TriggerModeSelectDTO();
            selectDTO.setValue(triggerMode.getValue());
            selectDTO.setTitle(triggerMode.getTitle());
            list.add(selectDTO);
        }
        return JsonResult.createSuccessResult(list);
    }

    /**
     * @api {GET} /common/getRequestMethodList 获取请求方式列表
     * @apiVersion 1.0.0
     * @apiGroup CommonController
     * @apiName getRequestMethodList
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Array} data 数据
     * @apiSuccessExample 响应结果示例
     * {"code":200,"msg":null,"data":["GET","POST","PUT","DELETE"]}
     */
    @GetMapping(value = "getRequestMethodList")
    public JsonResult<RequestMethod[]> getRequestMethodList() {
        RequestMethod[] requestMethods = RequestMethod.values();
        return JsonResult.createSuccessResult(requestMethods);
    }


    /**
     * @api {GET} /common/getRequestContentTypeList 获取请求contentType列表
     * @apiVersion 1.0.0
     * @apiGroup CommonController
     * @apiName getRequestContentTypeList
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Array} data 数据
     * @apiSuccessExample 响应结果示例
     * {"code":200,"msg":null,"data":["application/xml","application/json","application/x-www-form-urlencoded","multipart/form-data","application/octet-stream","text/plain","e/b"]}
     */
    @GetMapping(value = "getRequestContentTypeList")
    public JsonResult<List<String>> getRequestContentTypeList() {
        List<String> list = new ArrayList<>();
        for (RequestContentType requestContentType : RequestContentType.values()) {
            list.add(requestContentType.getName());
        }
        return JsonResult.createSuccessResult(list);
    }

    /**
     * @api {GET} /common/getAssertionTypeList 获取断言类型列表
     * @apiVersion 1.0.0
     * @apiGroup CommonController
     * @apiName getAssertionTypeList
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Array} data 数据
     * @apiSuccess (响应结果) {String} data.value 值
     * @apiSuccess (响应结果) {String} data.title 标题
     * @apiSuccessExample 响应结果示例
     * {"code":200,"msg":null,"data":[{"value":"condition","title":"数学表达式"},{"value":"eq","title":"相等"},{"value":"ne","title":"不相等"},{"value":"contains","title":"包含"},{"value":"notcontains","title":"不包含"},{"value":"regex","title":"正则匹配"}]}
     */
    @GetMapping(value = "getAssertionTypeList")
    public JsonResult<List<AssertionTypeSelectDTO>> getAssertionTypeList() {
        List<AssertionTypeSelectDTO> list = new ArrayList<>();
        for (AssertionType assertionType : AssertionType.values()) {
            AssertionTypeSelectDTO selectDTO = new AssertionTypeSelectDTO();
            selectDTO.setValue(assertionType.name());
            selectDTO.setTitle(assertionType.getTitle());
            list.add(selectDTO);
        }
        return JsonResult.createSuccessResult(list);
    }


    /**
     * @api {GET} /common/getDataPreparationTypeList 获取数据准备类型列表
     * @apiVersion 1.0.0
     * @apiGroup CommonController
     * @apiName getDataPreparationTypeList
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Array} data 数据
     * @apiSuccessExample 响应结果示例
     * {"code":200,"msg":null,"data":["text","csv","resource"]}
     */
    @GetMapping(value = "getDataPreparationTypeList")
    public JsonResult<DataPreparationType[]> getDataPreparationTypeList() {
        DataPreparationType[] dataPreparationTypes = DataPreparationType.values();
        return JsonResult.createSuccessResult(dataPreparationTypes);
    }


    /**
     * @api {GET} /common/getReportFailReasonList 获取报表错误原因下拉列表
     * @apiVersion 1.0.0
     * @apiGroup CommonController
     * @apiName getReportFailReasonList
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Array} data 数据
     * @apiSuccess (响应结果) {Number} data.value 值
     * @apiSuccess (响应结果) {String} data.title 名称
     * @apiSuccess (响应结果) {Boolean} data.needFixFlag 是否需要选择修复状态
     * @apiSuccessExample 响应结果示例
     * {"msg":"tMMxSuzy","code":7316,"data":[{"needFixFlag":true,"title":"rBuv","value":1280}]}
     */
    @GetMapping(value = "getReportFailReasonList")
    public JsonResult<List<ReportFailReasonSelectDTO>> getReportFailReasonList() {
        List<ReportFailReasonSelectDTO> list = new ArrayList<>();
        for (ReportFailReason reason : ReportFailReason.values()) {
            ReportFailReasonSelectDTO selectDTO = new ReportFailReasonSelectDTO();
            selectDTO.setValue(reason.getValue());
            selectDTO.setTitle(reason.getTitle());
            selectDTO.setNeedFixFlag(reason.getNeedFixFlag());
            list.add(selectDTO);
        }
        return JsonResult.createSuccessResult(list);
    }


    /**
     * @api {GET} /common/getSelectProjectList 获取项目下拉列表
     * @apiVersion 1.0.0
     * @apiGroup CommonController
     * @apiName getSelectProjectList
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Array} data 数据
     * @apiSuccess (响应结果) {Number} data.id 主键
     * @apiSuccess (响应结果) {String} data.name 名字
     * @apiSuccessExample 响应结果示例
     * {"msg":"Rnk7ovvp","code":5100,"data":[{"name":"KlM","id":1047}]}
     */
    @GetMapping(value = "getSelectProjectList")
    public JsonResult<List<ProjectSelectDTO>> getSelectProjectList() {
        return JsonResult.createSuccessResult(projectService.getSelectProjectList());
    }


    /**
     * @api {GET} /common/getSelectModuleList 获取模块下拉列表
     * @apiVersion 1.0.0
     * @apiGroup CommonController
     * @apiName getSelectModuleList
     * @apiParam (请求参数) {Number} projectId 项目ID
     * @apiParamExample 请求参数示例
     * projectId=7166
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Array} data 数据
     * @apiSuccess (响应结果) {Number} data.id 主键
     * @apiSuccess (响应结果) {String} data.name 名称
     * @apiSuccessExample 响应结果示例
     * {"msg":"i","code":4977,"data":[{"name":"1qpW3Qka","id":7394}]}
     */
    @GetMapping(value = "getSelectModuleList")
    public JsonResult<List<ModuleSelectDTO>> getSelectModuleList(Long projectId) {
        return JsonResult.createSuccessResult(moduleService.getSelectModuleList(projectId));
    }


    /**
     * @api {GET} /common/getSelectTestCasesList 获取测试任务下拉列表
     * @apiVersion 1.0.0
     * @apiGroup CommonController
     * @apiName getSelectTestCasesList
     * @apiParam (请求参数) {Number} moduleId 模块ID
     * @apiParamExample 请求参数示例
     * moduleId=5839
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Array} data 数据
     * @apiSuccess (响应结果) {Number} data.id 主键
     * @apiSuccess (响应结果) {String} data.name 名称
     * @apiSuccessExample 响应结果示例
     * {"code":200,"msg":null,"data":[{"id":1,"name":"开发测试-调用template22"},{"id":2,"name":"开发测试-上传文件"},{"id":3,"name":"长险-复星联合投保"},{"id":4,"name":"长险-复星联合投保-模板化"},{"id":5,"name":"保单提醒列表-提醒续费"},{"id":7,"name":"教你赚钱-获取banner"},{"id":8,"name":"教你赚钱-获取icon"},{"id":9,"name":"教你赚钱-获取认证信息-已认证"},{"id":10,"name":"教你赚钱-获取认证信息-未认证"},{"id":11,"name":"教你赚钱-获取大咖分享信息"},{"id":12,"name":"教你赚钱-获取大咖分享分页数据-20条"},{"id":14,"name":"教你赚钱-获取精选文章内容-20条"},{"id":16,"name":"教你赚钱-获取去赚钱出单信息"},{"id":17,"name":"去赚钱-新品推荐头部banner列表"},{"id":18,"name":"去赚钱-新品推荐产品列表"},{"id":19,"name":"易课堂推广位"}]}
     */
    @GetMapping(value = "getSelectTestCasesList")
    public JsonResult<List<TestCasesSelectDTO>> getSelectTestCasesList(Long moduleId) {
        return JsonResult.createSuccessResult(testCasesService.getSelectTestCasesList(moduleId));
    }

    /**
     * @api {GET} /common/getSelectUserList 获取用户下拉列表
     * @apiVersion 1.0.0
     * @apiGroup CommonController
     * @apiName getSelectUserList
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Array} data 数据
     * @apiSuccess (响应结果) {Number} data.id 主键
     * @apiSuccess (响应结果) {String} data.name 姓名
     * @apiSuccess (响应结果) {String} data.userName 登录名
     * @apiSuccessExample 响应结果示例
     * {"msg":"WjdXOy","code":2901,"data":[{"name":"JEIgR24VR","id":8711,"userName":"eRQo"}]}
     */
    @GetMapping(value = "getSelectUserList")
    public JsonResult<List<UserSelectDTO>> getSelectUserList() {
        return JsonResult.createSuccessResult(userService.getSelectUserList());
    }

    /**
     * @api {GET} /common/getSelectResourceConfigList 获取资源下拉列表
     * @apiVersion 1.0.0
     * @apiGroup CommonController
     * @apiName getSelectResourceConfigList
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Array} data 数据
     * @apiSuccess (响应结果) {Number} data.id
     * @apiSuccess (响应结果) {String} data.name
     * @apiSuccess (响应结果) {String} data.resourceType mysql,mongo,redis,mq
     * @apiSuccessExample 响应结果示例
     * {"code":200,"msg":null,"data":[{"id":1,"name":"baoxian","resourceType":"mysql"},{"id":2,"name":"mongo","resourceType":"mongo"},{"id":3,"name":"redis","resourceType":"redis"}]}
     */
    @GetMapping(value = "getSelectResourceConfigList")
    public JsonResult<List<ResourceConfigSelectDTO>> getSelectResourceConfigList() {
        return JsonResult.createSuccessResult(resourceConfigService.getSelectResourceConfigList());
    }

    /**
     * @api {GET} /common/getSelectActionTemplateList 获取模板下拉列表
     * @apiVersion 1.0.0
     * @apiGroup CommonController
     * @apiName getSelectActionTemplateList
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Array} data 数据
     * @apiSuccess (响应结果) {Number} data.id 模板ID
     * @apiSuccess (响应结果) {String} data.name 模板名称
     * @apiSuccess (响应结果) {Array} data.params 模板参数名称
     * @apiSuccessExample 响应结果示例
     * {"msg":"uo1","code":6181,"data":[{"name":"O","id":450,"params":["b8nO6","VcKE3Kq"]}]}
     */
    @GetMapping(value = "getSelectActionTemplateList")
    public JsonResult<List<ActionTemplateSelectDTO>> getSelectActionTemplateList() {
        return JsonResult.createSuccessResult(actionTemplateService.getSelectActionTemplateList());
    }

    /**
     * @apiDescription 文件流（file）
     * @api {POST} /common/uploadFile 上传文件
     * @apiVersion 1.0.0
     * @apiGroup CommonController
     * @apiName uploadFile
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccess (响应结果) {String} data.url 文件路径
     * @apiSuccessExample 响应结果示例
     * {"code":200,"msg":null,"data":{"url":"http://media.winbaoxian.com/testPlatform/20190401/455e47f8fa4d47919d9a90736a870318.jpg"}}
     */
    @PostMapping(value = "uploadFile")
    public JsonResult<UploadFileResultDTO> uploadFile(MultipartFile file) {
        String url = commonService.uploadFile(file);
        UploadFileResultDTO dto = new UploadFileResultDTO();
        dto.setUrl(url);
        return JsonResult.createSuccessResult(dto);
    }

    /**
     * @api {POST} /common/verifyText 校验文本
     * @apiVersion 1.0.0
     * @apiGroup CommonController
     * @apiName verifyText
     * @apiParam (请求体) {String} type 类型，action 操作; dataPreparation  数据准备;
     * @apiParam (请求体) {String} subType type的下的子分类，当type=action时，subType传actionType(HTTP、RESOURCE..)
     * @apiParam (请求体) {String} fieldName 校验的字段名字，如 sql 查询语句,requestHeader 请求头
     * @apiParam (请求体) {String} text 需要校验的文本
     * @apiParamExample 请求体示例
     * {"fieldName":"requestUrl","subType":"HTTP","text":"http://wwww.xx.com/${xx}","type":"action"}
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccessExample 响应结果示例
     * {"msg":"语法错误，请检查","code":400,"data":{}}
     */
    @PostMapping(value = "verifyText")
    public JsonResult verifyText(@RequestBody VerifyTextDTO dto) {
        commonService.verifyText(dto);
        return JsonResult.createSuccessResult("校验成功");
    }


    /**
     * @api {GET} /common/getScriptLangList 获取脚本语言列表
     * @apiVersion 1.0.0
     * @apiGroup CommonController
     * @apiName getScriptLangList
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Array} data 数据
     * @apiSuccessExample 响应结果示例
     * {"msg":"U1gSk","code":9305,"data":[]}
     */
    @GetMapping(value = "getScriptLangList")
    public JsonResult<ScriptLang[]> getScriptLangList() {
        ScriptLang[] scriptLangList = ScriptLang.values();
        return JsonResult.createSuccessResult(scriptLangList);
    }


}
