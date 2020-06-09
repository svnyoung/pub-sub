package com.svnyoung.pubsub.exception;

/**
 * @author: sunyang
 * @date: 2019/8/15 9:34
 * @version: 1.0
 * @since: 1.0
 * @see:
 */
public class MessageHandlerException extends BaseException {

    public MessageHandlerException() {
    }

    public MessageHandlerException(String message) {
        super(message);
    }

    public MessageHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageHandlerException(Throwable cause) {
        super(cause);
    }

    public MessageHandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
