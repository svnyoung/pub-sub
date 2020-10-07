package com.svnyoung.pubsub.message;

import com.svnyoung.pubsub.MessageSource;
import com.svnyoung.pubsub.spring.subscriber.handler.MessageHandler;

import java.io.Serializable;

public class MessageTemplate<T extends  Serializable> {

    private MessageSource messageSource;

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public MessageTemplate bindTopic(String topic){

        return this;
    }


    public <T> void publish(T msg){

    }

    public <T> void subscribe(MessageHandler messageHandler){

    }

}
