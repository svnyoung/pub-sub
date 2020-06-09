package com.svnyoung.pubsub.exception;

/**
 * @author: sunyang
 * @date: 2019/8/15 9:34
 * @version: 1.0
 * @since: 1.0
 * @see:
 */
public class SerializationException extends BaseException {

    public SerializationException() {
    }

    public SerializationException(String message) {
        super(message);
    }

    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }

    public SerializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
