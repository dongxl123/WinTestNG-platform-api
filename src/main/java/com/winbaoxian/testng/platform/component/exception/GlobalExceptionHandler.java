package com.winbaoxian.testng.platform.component.exception;

import com.winbaoxian.module.security.model.exceptions.WinSecurityException;
import com.winbaoxian.module.security.model.exceptions.WinSecurityUnAuthException;
import com.winbaoxian.testng.exception.WinTestNgException;
import com.winbaoxian.testng.platform.exception.WinTestNgPlatformException;
import com.winbaoxian.testng.platform.model.common.JsonResult;
import com.winbaoxian.testng.platform.model.constant.WinTestNGPlatformConstant;
import com.winbaoxian.testng.platform.model.enums.JsonResultCodeEnum;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 *
 * @Author DongXL
 * @Create 2016-12-07 17:54
 */

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus
    @ExceptionHandler(Exception.class)
    public Object handleCommonExp(Exception e) {
        logger.error("common exception handler  " + e.getMessage(), e);
        return JsonResult.createErrorResult("服务器内部问题");
    }

    @ResponseStatus
    @ExceptionHandler(WinSecurityException.class)
    public Object handleWinSecurityExp(WinSecurityException e) {
        logger.error("WinSecurity exception handler  " + e.getMessage());
        return JsonResult.createErrorResult(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(WinSecurityUnAuthException.class)
    public Object handleWinSecurityExp(WinSecurityUnAuthException e) {
        logger.error("winSecurity unAuthException handler  " + e.getMessage());
        return JsonResult.createNewInstance(JsonResultCodeEnum.UNAUTHORIZED, e.getMessage(), null);
    }

    @ResponseStatus
    @ExceptionHandler(WinTestNgPlatformException.class)
    public Object handleWinTestNgPlatformExp(WinTestNgPlatformException e) {
        logger.error("WinTestNgPlatform exception handler  " + e.getMessage());
        return JsonResult.createErrorResult(e.getMessage());
    }

    @ResponseStatus
    @ExceptionHandler(WinTestNgException.class)
    public Object handleWinTestNgExp(WinTestNgException e) {
        logger.error("WinTestNgException exception handler  " + e.getMessage());
        Throwable throwable = e.getCause();
        if (ClassUtils.getCanonicalName(throwable).startsWith(WinTestNGPlatformConstant.STRING_FREEMARKER)
                || StringUtils.startsWith(throwable.getMessage(), WinTestNGPlatformConstant.STRING_FREEMARKER)) {
            logger.error("WinTestNgException exception handler  ", e);
            return JsonResult.createErrorResult(throwable.getMessage());
        }
        return JsonResult.createErrorResult(e.getMessage());
    }

}
