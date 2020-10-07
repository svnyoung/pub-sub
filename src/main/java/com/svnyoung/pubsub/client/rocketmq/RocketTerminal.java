package com.svnyoung.pubsub.client.rocketmq;

import com.svnyoung.pubsub.AbstractTerminal;
import com.svnyoung.pubsub.Listener;
import com.svnyoung.pubsub.Serializer;
import com.svnyoung.pubsub.Subject;
import com.svnyoung.pubsub.exception.MessageClientException;
import com.svnyoung.pubsub.exception.MessageHandlerException;
import com.svnyoung.pubsub.message.DelayLevel;
import com.svnyoung.pubsub.message.Message;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: sunyang
 * @date: 2019/8/15 10:29
 * @version: 1.0
 * @since: 1.0
 * @see:
 */
public class RocketTerminal extends AbstractTerminal {

    protected  static final Logger logger= LoggerFactory.getLogger(RocketTerminal.class);

    public RocketTerminal(RocketMessageSource messageSource, Subject subject) {
        super(messageSource, subject);
    }

    private int maxMessageLength = 1024 * 1024 * 64;


    public int getMaxMessageLength() {
        return maxMessageLength;
    }

    public void setMaxMessageLength(int maxMessageLength) {
        this.maxMessageLength = maxMessageLength;
    }

    private final Map<String, DefaultMQProducer> producerMap = new ConcurrentHashMap<>();

    private final Map<String, MQPushConsumer> consumerMap = new ConcurrentHashMap<>();


    @Override
    public <T extends Serializable> void doSubscribe(String group, Listener<T> msgListener) throws MessageClientException {

        RocketMessageSource messageSource = (RocketMessageSource)this.getMessageSource();
        Subject subject = this.getSubject();
        if(consumerMap.get(group) != null){
            logger.warn("group：{}已经注册到到rocketMQ，Instance{}",group,messageSource.getInstanceName());
            return;
        }
        try {
            String rocketChooser = subject.getLabel();
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(group);
            consumer.setNamesrvAddr(messageSource.getNamesrvAddr());
            consumer.setConsumeThreadMax(messageSource.getConsumerThreadMax());
            consumer.setConsumeThreadMin(messageSource.getConsumerThreadMin());
            if (StringUtils.isNotBlank(messageSource.getInstanceName())) {
                consumer.setInstanceName(messageSource.getInstanceName());
            }
            consumer.setConsumeMessageBatchMaxSize(1);
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
            if (subject.getMessageModel() != null) {
                switch (subject.getMessageModel()) {
                    case BROADCASTING:
                        consumer.setMessageModel(MessageModel.BROADCASTING);
                        break;
                    case POINT_TO_POINT:
                        consumer.setMessageModel(MessageModel.CLUSTERING);
                        break;
                }
            }
            consumer.subscribe(subject.getTopic(), rocketChooser);
            this.registerListener(consumer,msgListener);
            consumer.start();
            consumerMap.put(group,consumer);
        } catch (MQClientException e) {
            logger.error("订阅消息失败,topic:{}", subject.getTopic(), e);
            throw new MessageClientException(e);
        }
    }

    protected <T extends Serializable> void registerListener(MQPushConsumer mqPushConsumer, Listener<T> msgListener){
        RocketMessageSource messageSource = (RocketMessageSource)this.getMessageSource();
        mqPushConsumer.registerMessageListener((List<MessageExt> msgs, ConsumeConcurrentlyContext context) -> {
               Serializer serializer = messageSource.getSerializer();
               Class<T> tClass = msgListener.getMessageClass();
               Message<T> message = new Message<>();
               message.setContent(serializer.deserialize(msgs.get(0).getBody(), tClass));
               message.setId(msgs.get(0).getMsgId());
               message.setSource(msgs.get(0));
               try {
                   this.onMsgHandler(message,msgListener);
               } catch (Exception e) {
                   logger.error("消费失败", e);
                   return ConsumeConcurrentlyStatus.RECONSUME_LATER;
               }
               return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
    }

    @Override
    public <T extends Serializable> void doPublish(String group, T msg, DelayLevel delayLevel) throws MessageClientException {
        DefaultMQProducer producer =this.gainMqProducer(group);
        RocketMessageSource messageSource = (RocketMessageSource)this.getMessageSource();
        byte[] bs = messageSource.getSerializer().serialize(msg);
        try {
            org.apache.rocketmq.common.message.Message message = new org.apache.rocketmq.common.message.Message(this.getSubject().getTopic(),StringUtils.defaultString(this.getSubject().getLabel(),StringUtils.EMPTY),bs);
            if(delayLevel != null){
                message.setDelayTimeLevel(delayLevel.getLevelId());
            }
            producer.send(message,messageSource.getSendTimeout());
        }catch (Exception e){
            logger.error("publish message fail,topic:{},content",this.getSubject().getTopic(),bs,e);
            throw new MessageClientException(e);
        }
    }

    @Override
    public <T extends Serializable> void doPublish(String group, T msg, long delayTime) throws MessageClientException {
        throw new MessageHandlerException("Rocket MQ 不支持指定时间发送");
    }

    protected DefaultMQProducer gainMqProducer(String group) throws MessageClientException {

        DefaultMQProducer producer = producerMap.get(group);
        RocketMessageSource messageSource = (RocketMessageSource)this.getMessageSource();
        try {
            if (producer == null) {
                producer = new DefaultMQProducer(group);
                producer.setMaxMessageSize(maxMessageLength);
                producer.setNamesrvAddr(messageSource.getNamesrvAddr());
                producer.setInstanceName(messageSource.getInstanceName());
                producer.start();
                producerMap.put(group,producer);
            }
        }catch (MQClientException e){
            logger.error("product:{}，启动失败",group);
            throw new MessageClientException(e);
        }
        return  producer;

    }



    @Override
    public void close() throws Exception {
        consumerMap.forEach((k,v)->{
            v.shutdown();
        });

        producerMap.forEach((k,v)->{
            v.shutdown();
        });

    }
}
