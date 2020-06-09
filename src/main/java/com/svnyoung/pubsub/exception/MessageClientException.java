package com.svnyoung.pubsub.exception;

/**
 * @author: sunyang
 * @date: 2019/8/15 9:34
 * @version: 1.0
 * @since: 1.0
 * @see:
 */
public class MessageClientException extends BaseException {

    public MessageClientException() {
    }

    public MessageClientException(String message) {
        super(message);
    }

    public MessageClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageClientException(Throwable cause) {
        super(cause);
    }

    public MessageClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
