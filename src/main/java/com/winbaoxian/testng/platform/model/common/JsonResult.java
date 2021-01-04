package com.winbaoxian.testng.platform.model.common;


import com.winbaoxian.testng.platform.model.enums.JsonResultCodeEnum;

/**
 * 返回前端数据格式
 */
public class JsonResult<T> {

    /**
     * 200 成功， 400 失败
     */
    private Integer code;
    /**
     * 提示信息
     */
    private String msg;
    /**
     * 数据
     */
    private T data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public static <T> JsonResult<T> createNewInstance(JsonResultCodeEnum codeEnum, String msg, T data) {
        JsonResult<T> result = new JsonResult<>();
        result.setCode(codeEnum.getValue());
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> JsonResult<T> createSuccessResult(T data) {
        return createNewInstance(JsonResultCodeEnum.SUCCESS, null, data);
    }

    public static JsonResult createSuccessResult(String msg) {
        return createNewInstance(JsonResultCodeEnum.SUCCESS, msg, null);
    }

    public static JsonResult createErrorResult(String msg) {
        return createNewInstance(JsonResultCodeEnum.FAIL, msg, null);
    }

}
