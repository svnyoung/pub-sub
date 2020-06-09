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
    private String chooser;



    @Override
    public int hashCode() {
        int result = topic != null ? topic.hashCode() : 0;
        result = 31 * result + (messageModel != null ? messageModel.hashCode() : 0);
        result = 31 * result + (chooser != null ? chooser.hashCode() : 0);
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

    public String getChooser() {
        return chooser;
    }

    public Subject setChooser(String chooser) {
        this.chooser = chooser;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}

        Subject subject = (Subject) o;

        if (topic != null ? !topic.equals(subject.topic) : subject.topic != null) {return false;}
        if (messageModel != subject.messageModel) {return false;}
        return chooser != null ? chooser.equals(subject.chooser) : subject.chooser == null;
    }
}
