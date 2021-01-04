package com.winbaoxian.testng.platform.controller;

import com.winbaoxian.testng.platform.model.common.JsonResult;
import com.winbaoxian.testng.platform.model.common.Pagination;
import com.winbaoxian.testng.platform.model.common.PaginationDTO;
import com.winbaoxian.testng.platform.model.dto.ActionTemplateDebugParams;
import com.winbaoxian.testng.platform.model.dto.ConsoleLogDTO;
import com.winbaoxian.testng.platform.model.dto.PlatformActionTemplateDTO;
import com.winbaoxian.testng.platform.model.dto.UuidDTO;
import com.winbaoxian.testng.platform.service.ActionTemplateService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 11:57
 */
@RestController
@RequestMapping(value = "actionTemplate")
public class ActionTemplateController {

    @Resource
    private ActionTemplateService actionTemplateService;

    /**
     * @api {POST} /actionTemplate/save 新增/修改测试模板
     * @apiVersion 1.0.0
     * @apiGroup ActionTemplateController
     * @apiName save
     * @apiParam (请求体) {Number} id
     * @apiParam (请求体) {String} name 接口名称
     * @apiParam (请求体) {String} description 描述
     * @apiParam (请求体) {Array} params 参数
     * @apiParam (请求体) {String} baseParams 基础数据
     * @apiParam (请求体) {Array} actions 内容
     * @apiParam (请求体) {String} result 结果，json格式
     * @apiParam (请求体) {Boolean} concurrentCacheSupport 并发支持，true:是，false:否
     * @apiParamExample 请求体示例
     * {"id":21,"creatorUid":2,"name":"APP登录","description":"APP登录","params":["a","b"],"actions":[{"name":"APP登录接口","description":null,"actionType":"HTTP","settings":"{\"requestBody\":\"{\\\"mobile\\\":\\\"${a}\\\",\\\"validateCode\\\":\\\"${b}\\\"}\",\"requestUrl\":\"http://service.winbaoxian.cn/salesUser/1/login\",\"requestMethod\":\"POST\",\"requestParams\":{\"t\":1,\"u\":\"0@winbaoxian.com\",\"di\":\"mockDeviceId\",\"m\":1,\"n\":1},\"requestHeader\":{},\"requestContentType\":\"e/b\"}","alias":"login","delayTimes":2000}],"result":"{\"token\":\"${login.body.r.token}\"}"}
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccessExample 响应结果示例
     * {"code":200,"msg":"操作成功","data":null}
     */
    @PostMapping(value = "save")
    public JsonResult save(@RequestBody PlatformActionTemplateDTO dto) {
        actionTemplateService.save(dto);
        return JsonResult.createSuccessResult("操作成功");
    }

    /**
     * @api {GET} /actionTemplate/page 分页获取测试模板列表
     * @apiVersion 1.0.0
     * @apiGroup ActionTemplateController
     * @apiName page
     * @apiDescription <ul><li><p>HTTP<code>{"requestHeader":"","requestUrl":"http://service.winbaoxian.cn/policy/1/getExpirePolicyCollate","requestParams":{"t":1,"n":1,"u":"10608088@winbaoxian.com","di":"mockDeviceId","m":1,"s":"${template.token}"},"requestMethod":"POST","requestContentType":"e/b","requestBody":"{\"pageNum\": 0}"}</code></p><table><thead><tr><th>字段</th><th>类型</th><th>描述</th></tr></thead><tbody><tr><td>requestHeader</td><td>Object</td><td>请求头</td></tr><tr><td>requestUrl</td><td>String</td><td>请求地址</td></tr><tr><td>requestParams</td><td>Object</td><td>请求参数</td></tr><tr><td>requestMethod</td><td>String</td><td>请求方式，调用接口获取列表</td></tr><tr><td>requestContentType</td><td>String</td><td>请求的content-type，调用接口获取列表</td></tr><tr><td>requestBody</td><td>String</td><td>请求体</td></tr></tbody></table></li><li><p>RESOURCE<code>{"resourceId":1,"sql":"SELECT id,`name`,mobile from sales_user limit 1","fetchOne":true}</code></p><table><thead><tr><th>字段</th><th>类型</th><th>描述</th></tr></thead><tbody><tr><td>resourceId</td><td>Number</td><td>资源ID，调用接口获取下拉列表</td></tr><tr><td>sql</td><td>String</td><td>查询语句</td></tr><tr><td>fetchOne</td><td>Boolean</td><td>是否返回单条数据（资源类型为mysql时有该参数）</td></tr></tbody></table></li><li><p>SETV<code>{"params":{"ip":"insurance2a-api.winbaoxian.cn","calculate":"/api/product/data/calculate","get_captcha":"/api/policy/getCaptcha","pre_order":"/api/policy/pre-order"}}</code></p><table><thead><tr><th>字段</th><th>类型</th><th>描述</th></tr></thead><tbody><tr><td>params</td><td>Object</td><td>参数</td></tr></tbody></table></li><li><p>ASSERTION<code>{"verifyList":[{"type":"condition","value1":"${getExpirePolicyCollate.body.r.policyExpireRemindList?exists?c}==true"},{"type":"eq","value1":"${getExpirePolicyCollate.body.r.policyExpireRemindList[0].policyUuid}","value2":"5108"}]}</code></p><table><thead><tr><th>字段</th><th>类型</th><th>描述</th></tr></thead><tbody><tr><td>type</td><td>String</td><td>断言类型，调用接口获取下拉列表</td></tr><tr><td>value1</td><td>String</td><td>不同断言涵义不一样</td></tr><tr><td>value2</td><td>String</td><td>不同断言涵义不一样</td></tr></tbody></table></li><li><p>TPL<code>{"templateId":1,"mappings":{"a":"${mobile}","b":"0000"}}</code></p><table><thead><tr><th>字段</th><th>类型</th><th>描述</th></tr></thead><tbody><tr><td>templateId</td><td>Number</td><td>模板ID</td></tr><tr><td>mappings</td><td>Object</td><td>模板入参数据映射</td></tr></tbody></table></li><li><p>GROUP<code>{"stepList":[]}</code></p><table><thead><tr><th>字段</th><th>类型</th><th>描述</th></tr></thead><tbody><tr><td>stepList</td><td>Array</td><td>子步骤列表</td></tr></tbody></table></li><li><p>IF<code>{"condition":"${a?c}","stepList":[]}</code></p><table><thead><tr><th>字段</th><th>类型</th><th>描述</th></tr></thead><tbody><tr><td>condition</td><td>String</td><td>判断条件</td></tr><tr><td>stepList</td><td>Array</td><td>子步骤列表</td></tr></tbody></table></li><li><p>FOR<code>{"iterData":"${toJSONString(list)}","iterAlias":"a","stepList":[]}</code></p><table><thead><tr><th>字段</th><th>类型</th><th>描述</th></tr></thead><tbody><tr><td>iterData</td><td>String</td><td>遍历数据</td></tr><tr><td>iterAlias</td><td>String</td><td>遍历别名</td></tr><tr><td>stepList</td><td>Array</td><td>子步骤列表</td></tr></tbody></table></li><li><p>SCRIPT<code>{"name":"脚本","actionType":"SCRIPT","lang":"PYTHON","content":"a=1+2","extractVars":["a"],"alias":"xx"}</code></p><table><thead><tr><th>字段</th><th>类型</th><th>描述</th></tr></thead><tbody><tr><td>lang</td><td>String</td><td>脚本语言类型</td></tr><tr><td>content</td><td>String</td><td>代码</td></tr><tr><td>extractVars</td><td>Array</td><td>提取的变量名</td></tr></tbody></table></li></ul>
     * @apiParam (请求参数) {String} name 名称，支持模糊查询
     * @apiParam (请求参数) {Number} pageNum 第几页
     * @apiParam (请求参数) {Number} pageSize 每页数量
     * @apiParamExample 请求参数示例
     * name=xrROcBNm&pageSize=1066&pageNum=2416
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccess (响应结果) {Array} data.list 列表
     * @apiSuccess (响应结果) {String} data.list.creatorName 创建人姓名
     * @apiSuccess (响应结果) {Number} data.list.id
     * @apiSuccess (响应结果) {Number} data.list.createTime 创建时间
     * @apiSuccess (响应结果) {Number} data.list.updateTime 更新时间
     * @apiSuccess (响应结果) {Number} data.list.creatorUid 创建人ID
     * @apiSuccess (响应结果) {String} data.list.name 接口名称
     * @apiSuccess (响应结果) {String} data.list.description 描述
     * @apiSuccess (响应结果) {Array} data.list.params 参数
     * @apiSuccess (响应结果) {String} data.list.baseParams 基础数据
     * @apiSuccess (响应结果) {Array} data.list.actions 内容
     * @apiSuccess (响应结果) {String} data.list.result 结果，json格式
     * @apiSuccess (响应结果) {Boolean} data.list.concurrentCacheSupport 并发支持，true:是，false:否
     * @apiSuccess (响应结果) {Boolean} data.list.deleted
     * @apiSuccess (响应结果) {Number} data.pageNum 第几页
     * @apiSuccess (响应结果) {Number} data.pageSize 每页数量
     * @apiSuccess (响应结果) {Number} data.totalRow 总数
     * @apiSuccess (响应结果) {Number} data.totalPage 总页数
     * @apiSuccess (响应结果) {String} data.orderProperty 排序字段
     * @apiSuccess (响应结果) {String} data.orderDirection 排序方式, DESC 降序;ASC 升序
     * @apiSuccessExample 响应结果示例
     * {"code":200,"msg":null,"data":{"pageNum":1,"pageSize":50,"totalRow":2,"totalPage":1,"orderProperty":null,"orderDirection":null,"list":[{"id":21,"createTime":1553022911000,"updateTime":1553028986000,"creatorUid":null,"name":"APP登录","description":"APP登录","params":["a","b"],"actions":[{"name":"APP登录接口","description":null,"actionType":"HTTP","settings":"{\"requestBody\":\"{\\\"mobile\\\":\\\"${a}\\\",\\\"validateCode\\\":\\\"${b}\\\"}\",\"requestUrl\":\"http://service.winbaoxian.cn/salesUser/1/login\",\"requestMethod\":\"POST\",\"requestParams\":{\"t\":1,\"u\":\"0@winbaoxian.com\",\"di\":\"mockDeviceId\",\"m\":1,\"n\":1},\"requestHeader\":\"\",\"requestContentType\":\"e/b\"}","alias":"login","delayTimes":2000}],"result":"{\"token\":\"${login.body.r.token}\"}","deleted":false,"creatorName":null},{"id":1,"createTime":1551219238000,"updateTime":1552510634000,"creatorUid":null,"name":"H5登录","description":"没地方联待开发可大幅度发是的三阶魔方点开始链接付款垃圾筐代理费","params":["a","b"],"actions":[{"name":"登录接口","description":null,"actionType":"HTTP","settings":"{\"requestBody\":\"{\\\"mobile\\\":\\\"${a}\\\",\\\"validateCode\\\":\\\"${b}\\\"}\",\"requestUrl\":\"http://test.winbaoxian.com/user/login/ajaxSave\",\"requestMethod\":\"POST\",\"requestHeader\":\"\",\"requestContentType\":\"application/x-www-form-urlencoded\"}","alias":"login","delayTimes":2000}],"result":"{\"token\":\"${login.cookies.token}\",\"JSESSIONID\":\"${login.cookies.token}\"}","deleted":false,"creatorName":null}],"startRow":0}}
     */
    @GetMapping(value = "page")
    public JsonResult<PaginationDTO<PlatformActionTemplateDTO>> page(String name, Pagination pagination) {
        return JsonResult.createSuccessResult(actionTemplateService.page(name, pagination));
    }

    /**
     * @api {POST} /actionTemplate/delete 删除测试模板
     * @apiVersion 1.0.0
     * @apiGroup ActionTemplateController
     * @apiName delete
     * @apiParam (请求参数) {Number} id 主键
     * @apiParamExample 请求参数示例
     * id=370
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccessExample 响应结果示例
     * {"msg":"4YJd15Tsv","code":9818,"data":{}}
     */
    @PostMapping(value = "delete")
    public JsonResult delete(Long id) {
        actionTemplateService.delete(id);
        return JsonResult.createSuccessResult("操作成功");
    }

    /**
     * @api {POST} /actionTemplate/debug 执行测试模板调试
     * @apiVersion 1.0.0
     * @apiGroup ActionTemplateController
     * @apiName debug
     * @apiParam (请求体) {Number} templateId 测试模板ID
     * @apiParam (请求体) {Object} mappings 参数值映射
     * @apiParamExample 请求体示例
     * {"templateId":1,"mappings":{"a":"15967126512","b":"0000"}}
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccess (响应结果) {String} data.uuid 唯一ID
     * @apiSuccessExample 响应结果示例
     * {"code":200,"msg":null,"data":{"uuid":"6a8a7901b2b64edc86a99a8bcbe0c664"}}
     */
    @PostMapping(value = "debug")
    public JsonResult<UuidDTO> debug(@RequestBody ActionTemplateDebugParams params) {
        String uuid = actionTemplateService.debug(params);
        UuidDTO dto = new UuidDTO();
        dto.setUuid(uuid);
        return JsonResult.createSuccessResult(dto);
    }


    /**
     * @api {GET} /actionTemplate/getDebugLog 获取调试日志
     * @apiVersion 1.0.0
     * @apiGroup ActionTemplateController
     * @apiName getDebugLog
     * @apiParam (请求参数) {String} uuid 唯一UUID
     * @apiParam (请求参数) {Number} offset 日志偏移量，初始为0，传上次请求返回的值
     * @apiParamExample 请求参数示例
     * offset=4950&uuid=gkNGiC5
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccess (响应结果) {Number} data.offset 日志偏移量，初始为0
     * @apiSuccess (响应结果) {String} data.content 内容
     * @apiSuccess (响应结果) {Boolean} data.endFlag 是否执行完， true:已完成
     * @apiSuccessExample 响应结果示例
     * {"msg":"QghjD","code":5099,"data":{"offset":1040,"endFlag":false,"content":"m"}}
     */
    @GetMapping(value = "getDebugLog")
    public JsonResult<ConsoleLogDTO> getDebugLog(String uuid, Long offset) {
        ConsoleLogDTO dto = actionTemplateService.getDebugLog(uuid, offset);
        return JsonResult.createSuccessResult(dto);
    }

}
