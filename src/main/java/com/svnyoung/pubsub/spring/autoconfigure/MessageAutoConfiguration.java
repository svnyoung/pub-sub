package com.svnyoung.pubsub.spring.autoconfigure;

import com.svnyoung.pubsub.MessageSource;
import com.svnyoung.pubsub.spring.context.MessageContextAwareBeanPostProcessor;
import com.svnyoung.pubsub.spring.listener.handler.ListenersAnnotationBeanPostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author: sunyang
 * @date: 2019/8/17 11:16
 * @version: 1.0
 * @since: 1.0
 * @see:
 */
@Configuration
public class MessageAutoConfiguration implements InitializingBean, DisposableBean{

    protected  static final Logger logger= LoggerFactory.getLogger(MessageAutoConfiguration.class);

    @Autowired(required = false)
    private List<MessageSource> messageSourceList;

    @Bean
    public ListenersAnnotationBeanPostProcessor listenersAnnotationBeanPostProcessor() {
        return new ListenersAnnotationBeanPostProcessor();
    }

    @Bean
    public MessageContextAwareBeanPostProcessor messageContextAwareBeanPostProcessor() {
        return new MessageContextAwareBeanPostProcessor();
    }


    @Override
    public void destroy() throws Exception {
        if(CollectionUtils.isEmpty(messageSourceList) == false){
            messageSourceList.forEach(messageSource -> {
                try {
                    messageSource.destroy();
                }catch (Exception e){
                    logger.error("destroy fail !",e);
                }
            });
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(CollectionUtils.isEmpty(messageSourceList) == false){
            messageSourceList.forEach(messageSource -> {
                try {
                    messageSource.init();
                }catch (Exception e){
                    logger.error("init fail !",e);
                }
            });
        }
    }
}
