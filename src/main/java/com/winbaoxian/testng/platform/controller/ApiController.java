package com.winbaoxian.testng.platform.controller;

import com.winbaoxian.testng.platform.model.common.JsonResult;
import com.winbaoxian.testng.platform.model.common.Pagination;
import com.winbaoxian.testng.platform.model.common.PaginationDTO;
import com.winbaoxian.testng.platform.model.dto.ApiSearchParams;
import com.winbaoxian.testng.platform.model.dto.IdParamDTO;
import com.winbaoxian.testng.platform.model.dto.PlatformApiDTO;
import com.winbaoxian.testng.platform.service.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * (Api)Controller类
 *
 * @author dongxuanliang252
 * @date 2020-05-26 09:17:38
 */
@RestController
@RequestMapping(value = "api")
@Slf4j
public class ApiController {

    @Resource
    private ApiService apiService;

    /**
     * @api {POST} /api/save 创建/保存接口数据
     * @apiVersion 1.0.0
     * @apiGroup ApiController
     * @apiName save
     * @apiParam (请求体) {Number} id
     * @apiParam (请求体) {String} apiUrl 地址
     * @apiParam (请求体) {String} apiTitle 标题
     * @apiParam (请求体) {Number} projectId 项目ID
     * @apiParam (请求体) {Boolean} targetFlag 目标标识，true：是 ; false：否
     * @apiParamExample 请求体示例
     * {"apiUrl":"w7mj","apiTitle":"T","targetFlag":true,"id":983,"projectId":1150}
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccessExample 响应结果示例
     * {"msg":"a3D","code":8884,"data":{}}
     */
    @PostMapping(value = "save")
    public JsonResult save(@RequestBody PlatformApiDTO dto) {
        apiService.save(dto);
        return JsonResult.createSuccessResult("操作成功");
    }

    /**
     * @api {GET} /api/page 分页查询接口数据
     * @apiVersion 1.0.0
     * @apiGroup ApiController
     * @apiName page
     * @apiParam (请求参数) {Number} projectId 项目ID
     * @apiParam (请求参数) {Boolean} targetFlag 目标标识，true：是 ; false：否
     * @apiParam (请求参数) {Boolean} finishFlag 是否已完成，true：已完成 ; false：未完成
     * @apiParam (请求参数) {String} searchKey 搜索词，支持模糊搜索
     * @apiParam (请求参数) {Number} pageNum 第几页
     * @apiParam (请求参数) {Number} pageSize 每页数量
     * @apiParamExample 请求参数示例
     * finishFlag=true&targetFlag=true&pageSize=1614&searchKey=jjIN&projectId=3965&pageNum=4652
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccess (响应结果) {Array} data.list 列表
     * @apiSuccess (响应结果) {Number} data.list.id
     * @apiSuccess (响应结果) {Number} data.list.createTime 创建时间
     * @apiSuccess (响应结果) {Number} data.list.updateTime 更新时间
     * @apiSuccess (响应结果) {String} data.list.apiUrl 地址
     * @apiSuccess (响应结果) {String} data.list.apiTitle 标题
     * @apiSuccess (响应结果) {Number} data.list.projectId 项目ID
     * @apiSuccess (响应结果) {Boolean} data.list.targetFlag 目标标识，true：是 ; false：否
     * @apiSuccess (响应结果) {Boolean} data.list.finishFlag 是否已完成，true：已完成 ; false：未完成
     * @apiSuccess (响应结果) {Boolean} data.list.deleted 是否删除， 1:删除， 0:有效
     * @apiSuccess (响应结果) {Number} data.pageNum 第几页
     * @apiSuccess (响应结果) {Number} data.pageSize 每页数量
     * @apiSuccess (响应结果) {Number} data.totalRow 总数
     * @apiSuccess (响应结果) {Number} data.totalPage 总页数
     * @apiSuccess (响应结果) {String} data.orderProperty 排序字段
     * @apiSuccess (响应结果) {String} data.orderDirection 排序方式, DESC 降序;ASC 升序
     * @apiSuccessExample 响应结果示例
     * {"msg":"Wv8LQyAC","code":442,"data":{"totalRow":5191,"totalPage":1561,"pageSize":2338,"orderDirection":"qz3T8SmQ","list":[{"finishFlag":false,"deleted":false,"apiUrl":"Nt9iJJW","createTime":981218830361,"apiTitle":"qZBGvawXSH","targetFlag":false,"updateTime":2024210881639,"id":1128,"projectId":7491}],"pageNum":3652,"orderProperty":"mYALEH"}}
     */
    @GetMapping(value = "page")
    public JsonResult<PaginationDTO<PlatformApiDTO>> page(ApiSearchParams params, Pagination pagination) {
        return JsonResult.createSuccessResult(apiService.page(params, pagination));
    }

    /**
     * @api {POST} /api/delete 删除接口数据
     * @apiVersion 1.0.0
     * @apiGroup ApiController
     * @apiName delete
     * @apiParam (请求体) {Number} id 主键
     * @apiParamExample 请求体示例
     * {"id":3274}
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccessExample 响应结果示例
     * {"msg":"ykjg","code":9390,"data":{}}
     */
    @PostMapping(value = "delete")
    public JsonResult delete(@RequestBody IdParamDTO paramDTO) {
        apiService.delete(paramDTO.getId());
        return JsonResult.createSuccessResult("操作成功");
    }

    /**
     * @api {POST} /api/upload 上传数据
     * @apiVersion 1.0.0
     * @apiGroup ApiController
     * @apiName upload
     * @apiParam (请求参数) {Number} projectId 项目ID
     * @apiParam (请求参数) {Object} file 文件流
     * @apiParamExample 请求参数示例
     * projectId=575
     * @apiSuccess (响应结果) {Number} code 200 成功， 400 失败
     * @apiSuccess (响应结果) {String} msg 提示信息
     * @apiSuccess (响应结果) {Object} data 数据
     * @apiSuccessExample 响应结果示例
     * {"msg":"5PQVH","code":5345,"data":{}}
     */
    @PostMapping(value = "upload")
    public JsonResult upload(Long projectId, MultipartFile file) {
        apiService.upload(projectId, file);
        return JsonResult.createSuccessResult("操作成功");
    }

    /**
     * @api {GET} /api/export 导出数据
     * @apiVersion 1.0.0
     * @apiGroup ApiController
     * @apiName export
     * @apiParam (请求参数) {Number} projectId 项目ID
     * @apiParam (请求参数) {Boolean} targetFlag 目标标识，true：是 ; false：否
     * @apiParam (请求参数) {Boolean} finishFlag 是否已完成，true：已完成 ; false：未完成
     * @apiParam (请求参数) {String} searchKey 搜索词，支持模糊搜索
     * @apiParamExample 请求参数示例
     * finishFlag=false&targetFlag=true&searchKey=AB8Ub&projectId=6556
     * @apiSuccess (响应结果) {Object} response
     * @apiSuccessExample 响应结果示例
     * null
     */
    @GetMapping(value = "export")
    public void export(ApiSearchParams params) {
        apiService.export(params);
    }

}