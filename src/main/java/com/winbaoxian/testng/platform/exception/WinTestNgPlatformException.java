package com.winbaoxian.testng.platform.exception;

/**
 * @author dongxuanliang252
 * @date 2019-03-07 14:54
 */
public class WinTestNgPlatformException extends RuntimeException {

    public WinTestNgPlatformException(String message) {
        super(message);
    }

    public WinTestNgPlatformException(Throwable cause) {
        super(cause);
    }

    public WinTestNgPlatformException(String message, Throwable cause) {
        super(message, cause);
    }
}
