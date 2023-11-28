package com.yuyu.utils.myExcption;

/**
 * @author yuyu
 * 用于抛出没有参数的异常
 */
public class NullParamException extends RuntimeException{
    public NullParamException() {
    }

    public NullParamException(String message) {
        super(message);
    }

    public NullParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullParamException(Throwable cause) {
        super(cause);
    }

    public NullParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
