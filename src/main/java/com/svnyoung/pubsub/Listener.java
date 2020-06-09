package com.svnyoung.pubsub;

import com.svnyoung.pubsub.message.Message;

import java.io.Serializable;

/**
 * @author: sunyang
 * @date: 2019/8/12 15:52
 * @version: 1.0
 * @since: 1.0
 * @see:
 */
public interface Listener<T extends Serializable> {


    /**
     * 接收消息内容
     * @date 2019/8/12 15:53
     * @param message 消息
     * @return 
     * @throws 
     */
    Reply onMsg(Message<T> message);


    /**
     * 获取消息class
     * @date 2019/8/16 18:14
     * @param 
     * @return 
     * @throws 
     */
    Class<T> getMessageClass();

}
