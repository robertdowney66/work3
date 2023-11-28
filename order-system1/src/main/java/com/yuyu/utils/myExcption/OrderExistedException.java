package com.yuyu.utils.myExcption;

/**
 * @author yuyu
 * 用于抛出订单不存在的异常
 */
public class OrderExistedException extends RuntimeException{
    public OrderExistedException() {
    }

    public OrderExistedException(String message) {
        super(message);
    }

    public OrderExistedException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderExistedException(Throwable cause) {
        super(cause);
    }

    public OrderExistedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
