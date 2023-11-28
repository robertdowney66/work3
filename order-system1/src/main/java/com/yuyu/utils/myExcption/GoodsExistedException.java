package com.yuyu.utils.myExcption;

/**
 * @author yuyu
 * 用于抛出货物不存在的异常
 */
public class GoodsExistedException extends RuntimeException{
    public GoodsExistedException() {
    }

    public GoodsExistedException(String message) {
        super(message);
    }

    public GoodsExistedException(String message, Throwable cause) {
        super(message, cause);
    }

    public GoodsExistedException(Throwable cause) {
        super(cause);
    }

    public GoodsExistedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
