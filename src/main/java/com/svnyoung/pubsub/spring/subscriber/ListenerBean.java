package com.svnyoung.pubsub.spring.subscriber;

import com.svnyoung.pubsub.message.MessageModel;

/**
 * @author ：sunyang
 * @date ：2018/12/12 9:05
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class ListenerBean {

    /**topic**/
    private String topic;

    /**分组**/
    private String depend;

    /**消息源**/
    private String messageSource;

    /**选择器**/
    private String chooser;

    /**批量消费大小**/
    private int batchSize;
    /**
     * 消息类型
     * **/
    private MessageModel messageModel;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDepend() {
        return depend;
    }

    public void setDepend(String depend) {
        this.depend = depend;
    }

    public String getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(String messageSource) {
        this.messageSource = messageSource;
    }

    public String getChooser() {
        return chooser;
    }

    public void setChooser(String chooser) {
        this.chooser = chooser;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public MessageModel getMessageModel() {
        return messageModel;
    }

    public void setMessageModel(MessageModel messageModel) {
        this.messageModel = messageModel;
    }
}
