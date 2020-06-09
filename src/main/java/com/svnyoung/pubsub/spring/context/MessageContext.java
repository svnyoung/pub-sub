package com.svnyoung.pubsub.spring.context;

import com.svnyoung.pubsub.MessageSource;
import com.svnyoung.pubsub.Subject;
import com.svnyoung.pubsub.message.Message;

/**
 * @author: sunyang
 * @date: 2019/8/19 14:28
 * @version: 1.0
 * @since: 1.0
 * @see:
 */
public interface MessageContext {


    String MESSAGE_SOURCE_CONTEXT = "MESSAGE_SOURCE_CONTEXT";


    String SUBJECT_CONTEXT = "MESSAGE_SOURCE_CONTEXT";


    String MESSAGE_CONTEXT = "MESSAGE_CONTEXT";

    /**
     * 获取消息
     * @date 2019/8/19 17:16
     * @param 
     * @return 
     * @throws 
     */
    Message getMessage();


    /**
     * 获取消息源
     * @date 2019/8/19 14:33
     * @param 
     * @return 
     * @throws 
     */
    MessageSource getMessageSource();


    /**
     * 获取消息基本信息
     * @date 2019/8/19 14:33
     * @param
     * @return
     * @throws
     */
    Subject getSubject();




}
