 package com.svnyoung.pubsub.spring.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

/**
 * @author: sunyang
 * @date: 2019/8/19 14:50
 * @version: 1.0
 * @since: 1.0
 * @see:
 */
public class MessageContextAwareBeanPostProcessor implements BeanPostProcessor,  Ordered {



    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(MessageContextAware.class.isAssignableFrom(bean.getClass())){
            ((MessageContextAware)bean).setMessageContext(new GenericMessageContext());
        }
         return bean;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
