package com.svnyoung.pubsub.spring.context;

import com.svnyoung.pubsub.MessageSource;
import com.svnyoung.pubsub.Subject;
import com.svnyoung.pubsub.message.Message;

/**
 * @author: sunyang
 * @date: 2019/8/19 15:37
 * @version: 1.0
 * @since: 1.0
 * @see:
 */
public class GenericMessageContext implements MessageContext {


    @Override
    public MessageSource getMessageSource() {
        return (MessageSource) MessageContextHolder.getHolder().get(MESSAGE_SOURCE_CONTEXT);
    }

    @Override
    public Subject getSubject() {
        return (Subject) MessageContextHolder.getHolder().get(SUBJECT_CONTEXT);
    }

    @Override
    public Message getMessage() {
        return (Message) MessageContextHolder.getHolder().get(MESSAGE_CONTEXT);
    }
}
