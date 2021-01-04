package com.winbaoxian.testng.platform.controller;

import com.winbaoxian.testng.platform.model.common.JsonResult;
import com.winbaoxian.testng.platform.model.common.Pagination;
import com.winbaoxian.testng.platform.model.common.PaginationDTO;
import com.winbaoxian.testng.platform.model.dto.IdParamDTO;
import com.winbaoxian.testng.platform.model.dto.PlatformResourceConfigDTO;
import com.winbaoxian.testng.platform.service.ResourceConfigService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author dongxuanliang252
 * @date 2019-03-20 12:06
 */
@RestController
@RequestMapping(value = "/resourceConfig")
public class ResourceConfigController {

    @Resource
    private ResourceConfigService resourceConfigService;

    /**
     * @api {POST} /resourceConfig/save 新增/更新资源配置
     * @apiVersion 1.0.0
     * @apiGroup ResourceConfigController
     * @apiName save
     * @apiParam (请求体) {Number} id 主键
     * @apiParam (请求体) {String} name 名称
     * @apiParam (请求体) {String} description 描述
     * @apiParam (请求体) {String} resourceType mysql,mongo,redis,mq
     * @apiParam (请求体) {String} settings  资源配置数据，json字符串，不同资源类型，字段有区别. mysql:url 连接地址, userName 用户名，password 密码；mongo: url 连接地址; reids:host 地址， port 端口，database 数据库，password 密码; mq 暂不支持
     * @apiParamExample 请求体示例
     * {"description":"e4ibE","name":"5r","settings":"{\"url\":\"jdbc:mysql://testwinbx.mysql.rds.aliyuncs.com:3306/baoxian?allowMultiQueries=true\",\"userName\":\"winbx_test\",\"password\":\"funcitypt001\"}","id":1896,"resourceType":"mysql"}
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccessExample 响应结果示例
     * {"msg":"vX8YUQD","code":3256,"data":{}}
     */
    @PostMapping(value = "save")
    public JsonResult save(@RequestBody PlatformResourceConfigDTO dto) {
        resourceConfigService.save(dto);
        return JsonResult.createSuccessResult("操作成功");
    }

    /**
     * @api {GET} /resourceConfig/page 分页获取资源配置
     * @apiVersion 1.0.0
     * @apiGroup ResourceConfigController
     * @apiName page
     * @apiParam (请求参数) {String} resourceType 资源类型，不选择为null
     * @apiParam (请求参数) {String} name 名称，支持模糊查询
     * @apiParam (请求参数) {Number} pageNum 第几页
     * @apiParam (请求参数) {Number} pageSize 每页数量
     * @apiParamExample 请求参数示例
     * name=xPjhfT5&pageSize=7642&pageNum=1785&resourceType=o5PiD
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccess (响应结果) {Array} data.list 列表
     * @apiSuccess (响应结果) {Number} data.list.id 主键
     * @apiSuccess (响应结果) {Number} data.list.createTime 创建时间
     * @apiSuccess (响应结果) {Number} data.list.updateTime 更新时间
     * @apiSuccess (响应结果) {String} data.list.name 名称
     * @apiSuccess (响应结果) {String} data.list.description 描述
     * @apiSuccess (响应结果) {String} data.list.resourceType mysql,mongo,redis,mq
     * @apiSuccess (响应结果) {String} data.list.settings 资源配置数据
     * @apiSuccess (响应结果) {Number} data.pageNum 第几页
     * @apiSuccess (响应结果) {Number} data.pageSize 每页数量
     * @apiSuccess (响应结果) {Number} data.totalRow 总数
     * @apiSuccess (响应结果) {Number} data.totalPage 总页数
     * @apiSuccess (响应结果) {String} data.orderProperty 排序字段
     * @apiSuccess (响应结果) {String} data.orderDirection 排序方式, DESC 降序;ASC 升序
     * @apiSuccessExample 响应结果示例
     * {"code":200,"msg":null,"data":{"pageNum":1,"pageSize":50,"totalRow":3,"totalPage":1,"orderProperty":"createTime","orderDirection":"DESC","list":[{"id":3,"createTime":1552661293000,"updateTime":1552663643000,"name":"redis","description":null,"resourceType":"redis","settings":"{\"host\":\"120.26.106.94\",\"port\":3736,\"database\":8,\"password\":\"D6eAtdgwdzSdtUBz3xFQ5bFCysaisf\"}","deleted":false},{"id":2,"createTime":1552341132000,"updateTime":1552343824000,"name":"mongo","description":null,"resourceType":"mongo","settings":"{\"url\":\"mongodb://wyxx_app:nfrqT1dbyzfh8w0irKHEE7wdmkUUM1RO@121.40.88.39:3717,121.41.53.215:27017/wyxx_app\"}\r\n","deleted":false},{"id":1,"createTime":1551997782000,"updateTime":1551999245000,"name":"baoxian","description":null,"resourceType":"mysql","settings":"{\"url\":\"jdbc:mysql://testwinbx.mysql.rds.aliyuncs.com:3306/baoxian?allowMultiQueries=true\",\"userName\":\"winbx_test\",\"password\":\"funcitypt001\"}","deleted":false}],"startRow":0}}
     */
    @GetMapping(value = "page")
    public JsonResult<PaginationDTO<PlatformResourceConfigDTO>> page(String resourceType, String name, Pagination pagination) {
        return JsonResult.createSuccessResult(resourceConfigService.page(resourceType, name, pagination));
    }

    /**
     * @api {POST} /resourceConfig/delete 删除资源配置
     * @apiVersion 1.0.0
     * @apiGroup ResourceConfigController
     * @apiName delete
     * @apiParam (请求体) {Number} id 主键
     * @apiParamExample 请求体示例
     * {"id":8230}
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccessExample 响应结果示例
     * {"msg":"j0gt3C","code":3158,"data":{}}
     */
    @PostMapping(value = "delete")
    public JsonResult delete(@RequestBody IdParamDTO paramDTO) {
        resourceConfigService.delete(paramDTO.getId());
        return JsonResult.createSuccessResult("操作成功");
    }

}
