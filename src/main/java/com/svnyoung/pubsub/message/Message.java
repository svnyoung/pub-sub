package com.svnyoung.pubsub.message;

import java.io.Serializable;

/**
 * @author: sunyang
 * @date: 2019/8/2 13:39
 * @version: 1.0
 * @since: 1.0
 * @see:
 */
public class Message<T extends Serializable> {


    private String id;

    /**
     * 内容
     * @date 2019/8/10 13:47
     */
    private T content;

    private Object source;

    public Message setId(String id) {
        this.id = id;
        return this;
    }

    public Message setContent(T content) {
        this.content = content;
        return this;
    }

    public String getId() {
        return id;
    }

    public T getContent() {
        return content;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
}
