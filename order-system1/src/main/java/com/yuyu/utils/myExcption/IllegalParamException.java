package com.yuyu.utils.myExcption;

/**
 * @author yuyu
 * 用于抛出传入非法参数的异常
 */
public class IllegalParamException extends RuntimeException{
    public IllegalParamException() {
    }

    public IllegalParamException(String message) {
        super(message);
    }

    public IllegalParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalParamException(Throwable cause) {
        super(cause);
    }

    public IllegalParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
