package com.svnyoung.pubsub;

import com.svnyoung.pubsub.exception.MessageClientException;
import com.svnyoung.pubsub.message.DelayLevel;

import java.io.Serializable;

/**
 *
 * Jms终端
 * @author: sunyang
 * @date: 2019/8/3 10:06
 * @version: 1.0
 * @since: 1.0
 * @see:
 */
public interface Terminal extends AutoCloseable {



    /**
     * 订阅
     * @param msgListener 监听器
     * @throws MessageClientException
     * **/
    <T extends Serializable> void subscribe(Listener<T> msgListener) throws MessageClientException;


    /**
     * 批量订阅
     * @param msgListener 监听器
     * @param bathSize 批次大小
     * @throws MessageClientException
     * **/
    <T extends Serializable> void subscribe(int bathSize,Listener<T> msgListener) throws MessageClientException;



    /**
     * 订阅
     * @param group
     * @param msgListener 监听器
     * @throws MessageClientException
     * **/
    <T extends Serializable> void subscribe(String group, Listener<T> msgListener) throws MessageClientException;



    /**
     * 发布消息
     * @date 2019/8/14 9:13
     * @param msg 消息
     * @return
     * @throws MessageClientException
     */
    <T extends Serializable> void publish(T msg) throws MessageClientException;


    /**
     * 发布消息
     * @date 2019/8/14 9:13
     * @param msg 消息
     * @param delayLevel 延迟消息级别
     * @return
     * @throws MessageClientException
     */
    <T extends Serializable> void   publish(T msg, DelayLevel delayLevel) throws MessageClientException;


    /**
     * 发布消息
     * @date 2019/8/14 9:13
     * @param msg 消息
     * @param delayTime 延迟消息时间
     * @return
     * @throws MessageClientException
     */
    <T extends Serializable> void publish(T msg, long delayTime) throws MessageClientException;


    /**
     * 发布消息
     * @date 2019/8/14 9:13
     * @param group 组
     * @param msg 消息
     * @return
     * @throws MessageClientException
     */
    <T extends Serializable> void publish(String group,T msg) throws MessageClientException;

    /**
     * 发布消息
     * @date 2019/8/14 9:13
     * @param group 组
     * @param msg 消息
     * @param delayLevel 延迟消息级别
     * @return
     * @throws MessageClientException
     */
    <T extends Serializable> void publish(String group,T msg, DelayLevel delayLevel) throws MessageClientException;


    /**
     * 发布消息
     * @date 2019/8/14 9:13
     * @param group 组
     * @param msg 消息
     * @param delayTime 延迟消息时间
     * @return
     * @throws MessageClientException
     */
    <T extends Serializable> void publish(String group,T msg, long delayTime) throws MessageClientException;




    /**
     * 获取信息源
     * @date 2019/8/19 14:47
     * @param
     * @return
     * @throws
     */
    MessageSource getMessageSource();



}
