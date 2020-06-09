package com.svnyoung.pubsub;

import com.svnyoung.pubsub.exception.MessageClientException;
import com.svnyoung.pubsub.exception.MessageHandlerException;
import com.svnyoung.pubsub.message.DelayLevel;
import com.svnyoung.pubsub.message.Message;
import com.svnyoung.pubsub.spring.context.MessageContext;
import com.svnyoung.pubsub.spring.context.MessageContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author: sunyang
 * @date: 2019/8/19 17:27
 * @version: 1.0
 * @since: 1.0
 * @see:
 */
public abstract class AbstractTerminal implements Terminal {

    protected  static final Logger logger= LoggerFactory.getLogger(AbstractTerminal.class);

    private Subject subject;

    private MessageSource messageSource;

    public final static String DEFAULT_PRODUCER_DEPEND = "DEFAULT_PRODUCER_DEPEND";

    public static final String DEFAULT_CONSUMER_DEPEND = "DEFAULT_CONSUMER_DEPEND";


    public AbstractTerminal(MessageSource messageSource, Subject subject) {
        this.subject = subject;
        this.messageSource = messageSource;
    }


    @Override
    public MessageSource getMessageSource() {
        return messageSource;
    }

    public Subject getSubject() {
        return subject;
    }

    @Override
    public <T extends Serializable> void subscribe(Listener<T> msgListener) throws MessageClientException {
        this.subscribe(DEFAULT_CONSUMER_DEPEND,msgListener);
    }

    @Override
    public <T extends Serializable> void subscribe(int bathSize, Listener<T> msgListener) throws MessageClientException {
        this.subscribe(DEFAULT_CONSUMER_DEPEND,msgListener);
    }

    @Override
    public  <T extends Serializable> void subscribe(String depend, Listener<T> msgListener) throws MessageClientException {
        this.doSubscribe(depend,msgListener);
    }

    /**
     * 订阅消息
     * @date 2019/8/19 18:04
     * @param depend 分组
     * @param msgListener  监听器
     * @return
     * @throws MessageClientException
     */
    public abstract  <T extends Serializable> void doSubscribe(String depend, Listener<T> msgListener) throws MessageClientException;


    @Override
    public <T extends Serializable> void publish(T msg) throws MessageClientException {
        this.publish(DEFAULT_PRODUCER_DEPEND,msg);
    }

    @Override
    public <T extends Serializable>  void  publish(String depend, T msg) throws MessageClientException {
        this.publish(depend,msg, DelayLevel.LEVEL00);
    }

    @Override
    public <T extends Serializable> void publish(T msg, DelayLevel delayLevel) throws MessageClientException {
        this.publish(DEFAULT_PRODUCER_DEPEND,msg,DelayLevel.LEVEL00);
    }

    @Override
    public <T extends Serializable> void publish(T msg, long delayTime) throws MessageClientException {
        this.publish(DEFAULT_PRODUCER_DEPEND,msg,delayTime);
    }

    @Override
    public <T extends Serializable> void publish(String depend,T msg, DelayLevel delayLevel) throws MessageClientException {
        this.doPublish(depend,msg,delayLevel);
    }

    @Override
    public <T extends Serializable> void publish(String depend,T msg, long delayTime) throws MessageClientException {
        this.doPublish(depend,msg,delayTime);
    }


    /**
     * 执行发布
     * @date 2019/8/19 17:57
     * @param depend 分组
     * @param delayLevel 级别
     * @param msg 内容
     * @return
     * @throws MessageClientException
     */
    protected  abstract <T extends Serializable> void doPublish(String depend,T msg, DelayLevel delayLevel) throws MessageClientException;


    /**
     * 执行发布
     * @date 2019/8/19 17:57
     * @param depend 分组
     * @param delayTime 时间
     * @param msg 内容
     * @return
     * @throws MessageClientException
     */
    protected abstract <T extends Serializable> void doPublish(String depend,T msg, long delayTime) throws MessageClientException;


    protected  <T extends Serializable> void onMsgHandler( Message<T> message,
                                                           Listener<T> msgListener) throws MessageHandlerException {

        MessageContextHolder.getHolder().put(MessageContext.MESSAGE_CONTEXT,message);
        MessageContextHolder.getHolder().put(MessageContext.MESSAGE_SOURCE_CONTEXT,this.getMessageSource());
        MessageContextHolder.getHolder().put(MessageContext.SUBJECT_CONTEXT,this.getSubject());

        try {
            Reply reply =  msgListener.onMsg(message);
            if(reply != Reply.SUCCESS){
                this.publish(message.getContent(),DelayLevel.getByLevelId(reply.getCode()));
            }
        } catch (Throwable e) {
            logger.error("consumer message error",e);
            throw  new MessageHandlerException(e);
        }finally {
            MessageContextHolder.remove();
        }
    }


}
