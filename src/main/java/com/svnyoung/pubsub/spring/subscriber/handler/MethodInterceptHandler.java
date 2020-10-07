package com.svnyoung.pubsub.spring.subscriber.handler;

import com.svnyoung.pubsub.Reply;
import com.svnyoung.pubsub.exception.MessageHandlerException;
import com.svnyoung.pubsub.spring.subscriber.Listener;
import com.svnyoung.pubsub.spring.subscriber.ListenerBean;
import com.svnyoung.pubsub.spring.subscriber.Listeners;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：sunyang
 * @date ：2018/12/11 19:45
 * @description：
 * @modified By：
 * @version: 1.0
 */

public final  class MethodInterceptHandler extends AbstractMessageHandler{

    protected  static final Logger logger= LoggerFactory.getLogger(MethodInterceptHandler.class);


    private HandlerMethod handlerMethod;

    private List<ListenerBean> listenerBeans;

    private Class<? extends Serializable> messageClass;

    @Override
    public Class<? extends Serializable> getMessageClass() {
        return messageClass;
    }

    public MethodInterceptHandler(HandlerMethod handlerMethod){
        Assert.notNull(handlerMethod,"handlerMethod is request");
        if(handlerMethod.getMethod().getReturnType().getDeclaringClass() != null &&
                !handlerMethod.getMethod().getReturnType().isAssignableFrom(Reply.class)){
            Assert.isTrue(false,"@Listeners return type must void or Reply");
        }
        Class<?>[] classesParameter = handlerMethod.getBridgedMethod().getParameterTypes();

        Assert.isTrue(ArrayUtils.isNotEmpty(classesParameter) &&
                classesParameter.length == 1,
                "参数不能为空，且必须是一个");
        Class<?> messageClass = classesParameter[0];
        this.messageClass = (Class<? extends Serializable>)messageClass;
        this.handlerMethod = handlerMethod;
        this.createSupportType(handlerMethod);

    }

    protected void createSupportType(HandlerMethod handlerMethod){
        Listeners listeners = handlerMethod.getMethodAnnotation(Listeners.class);
        Assert.notNull(listeners,"the Method Listeners is request");
        listenerBeans = new ArrayList<>();
        for(Listener listener : listeners.value()){
            ListenerBean listenerBean = new ListenerBean();
            listenerBean.setTopic(listener.topic());
            listenerBean.setChooser(listener.chooser());
            listenerBean.setDepend(listener.group());
            listenerBean.setMessageSource(listener.messageSource());
            listenerBean.setMessageModel(listener.messageModel());
            listenerBeans.add(listenerBean);
        }
    }

    @Override
    public List<ListenerBean> support() {
        return listenerBeans;
    }

    @Override
    public Reply onMsg(Serializable msg) throws MessageHandlerException {
        Reply mqReply;
        try {
            mqReply = (Reply) handlerMethod.doInvoke(msg);
        } catch (Exception e) {
            logger.error("Listener invoke fail",e);
            return Reply.RECONSUME_01;
        }
        return mqReply == null ? Reply.SUCCESS:mqReply;
    }
}
