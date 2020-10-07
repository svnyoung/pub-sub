package com.svnyoung.pubsub.client.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.svnyoung.pubsub.AbstractTerminal;
import com.svnyoung.pubsub.Listener;
import com.svnyoung.pubsub.Subject;
import com.svnyoung.pubsub.exception.MessageClientException;
import com.svnyoung.pubsub.message.DelayLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: sunyang
 * @date: 2019/8/15 10:29
 * @version: 1.0
 * @since: 1.0
 * @see:
 */
public class RabbitTerminal extends AbstractTerminal {

    protected  static final Logger logger= LoggerFactory.getLogger(RabbitTerminal.class);

    private Map<Subject, Channel> subjectChannelMap = new ConcurrentHashMap<>();


    private RabbitMessageSource rabbitMessageSource;

    public RabbitTerminal(RabbitMessageSource messageSource, Subject subject) {
        super(messageSource, subject);
        this.rabbitMessageSource = messageSource;
    }

    @Override
    public <T extends Serializable> void doSubscribe(String depend,
                                                     Listener<T> msgListener) throws MessageClientException {
        try {
            Connection connection = rabbitMessageSource.getConnectionFactory().newConnection();
            Optional<Channel> channel = connection.openChannel();
            channel.get().exchangeBind(this.getSubject().getTopic(),depend,this.getSubject().getLabel());
        }catch (Exception e){

        }
    }

    @Override
    protected <T extends Serializable> void doPublish(String depend, T msg, DelayLevel delayLevel) throws MessageClientException {

    }

    @Override
    protected <T extends Serializable> void doPublish(String depend, T msg, long delayTime) throws MessageClientException {

    }

    @Override
    public void close() throws Exception {

    }
}
