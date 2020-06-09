package com.svnyoung.pubsub.spring.context;

import org.springframework.beans.factory.Aware;

/**
 * @author: sunyang
 * @date: 2019/8/19 14:27
 * @version: 1.0
 * @since: 1.0
 * @see:
 */
public interface MessageContextAware extends Aware {


    /**
     * 设置message上下文
     * @date 2019/8/19 14:29
     * @param messageContext message上下文
     * @return 
     * @throws 
     */
    void setMessageContext(MessageContext messageContext);

}
