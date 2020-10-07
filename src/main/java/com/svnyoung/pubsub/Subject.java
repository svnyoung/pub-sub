package com.svnyoung.pubsub;

import com.svnyoung.pubsub.message.MessageModel;

import java.io.Serializable;

/**
 * @author: sunyang
 * @date: 2019/8/12 15:54
 * @version: 1.0
 * @since: 1.0
 * @see:
 */
public class Subject implements Serializable {

    /**
     * topic
     * @return
     * **/
    private String topic;

    /**
     * 消息类型
     * @return
     * **/
    private MessageModel messageModel;

    /**
     *
     * 选择器
     * @date 2019/8/13 10:02
     * @param
     * @return
     * @throws
     */
    private String label;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subject)) return false;

        Subject subject = (Subject) o;

        if (!getTopic().equals(subject.getTopic())) return false;
        if (getMessageModel() != subject.getMessageModel()) return false;
        return getLabel().equals(subject.getLabel());
    }

    @Override
    public int hashCode() {
        int result = getTopic().hashCode();
        result = 31 * result + getMessageModel().hashCode();
        result = 31 * result + getLabel().hashCode();
        return result;
    }

    public String getTopic() {
        return topic;
    }

    public Subject setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public MessageModel getMessageModel() {
        return messageModel;
    }

    public Subject setMessageModel(MessageModel messageModel) {
        this.messageModel = messageModel;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public Subject setLabel(String label) {
        this.label = label;
        return this;
    }

}
