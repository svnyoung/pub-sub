
package com.svnyoung.pubsub.spring.subscriber.handler;

import com.svnyoung.pubsub.MessageSource;
import com.svnyoung.pubsub.Reply;
import com.svnyoung.pubsub.Subject;
import com.svnyoung.pubsub.Terminal;
import com.svnyoung.pubsub.message.Message;
import com.svnyoung.pubsub.message.MessageModel;
import com.svnyoung.pubsub.spring.subscriber.Listener;
import com.svnyoung.pubsub.spring.subscriber.ListenerBean;
import com.svnyoung.pubsub.spring.subscriber.Listeners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sunyang
 * @date 2018/12/12 15:53
 * @version: 1.0
 * @see Listener
 **/
public class ListenersAnnotationBeanPostProcessor
        implements BeanPostProcessor, ApplicationListener<ContextRefreshedEvent>,BeanFactoryAware {

    protected  static final Logger logger= LoggerFactory.getLogger(ListenersAnnotationBeanPostProcessor.class);

    private final Set<Class<?>> nonAnnotatedClasses =
            Collections.newSetFromMap(new ConcurrentHashMap<>(64));

    private List<MessageHandler> messageHandlers;

    @Autowired(required = false)
    private Map<String, MessageSource> messageSourceMap;


    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * @param mqListener
     * @return
     * @throws
     * @date 2018/12/12 19:10
     * @see
     */
    protected void processMQListener(Listeners mqListener, Method method, Object bean, String beanName) {
        Method methodToUse = checkProxy(method, bean);
        HandlerMethod methodHandler = new HandlerMethod(bean, methodToUse);
        MessageHandler mqHandler = new MethodInterceptHandler(methodHandler);
        if (messageHandlers == null) {
            messageHandlers = new ArrayList<>();
        }
        this.messageHandlers.add(mqHandler);

    }


    private Method checkProxy(Method methodArg, Object bean) {
        Method method = methodArg;
        if (AopUtils.isJdkDynamicProxy(bean)) {
            try {
                method = bean.getClass().getMethod(method.getName(), method.getParameterTypes());
                Class<?>[] proxiedInterfaces = ((Advised) bean).getProxiedInterfaces();
                for (Class<?> iface : proxiedInterfaces) {
                    try {
                        method = iface.getMethod(method.getName(), method.getParameterTypes());
                        break;
                    } catch (NoSuchMethodException noMethod) {
                    }
                }
            } catch (SecurityException ex) {
                ReflectionUtils.handleReflectionException(ex);
            } catch (NoSuchMethodException ex) {
                throw new IllegalStateException(String.format(
                        "@Listeners method '%s' found on bean target class '%s', " +
                                "but not found in any interface(s) for bean JDK proxy. Either " +
                                "pull the method up to an interface or switch to subclass (CGLIB) " +
                                "proxies by setting proxy-target-class/proxyTargetClass " +
                                "attribute to 'true'", method.getName(), method.getDeclaringClass().getSimpleName()), ex);
            }
        }
        return method;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if(event.getApplicationContext().getParent() == null){
            if (CollectionUtils.isEmpty(messageHandlers) == false) {
                messageHandlers.forEach(messageHandler -> {
                    List<ListenerBean> listenerBeans = messageHandler.support();
                    for (ListenerBean listenerBean : listenerBeans) {
                        MessageSource messageSource = messageSourceMap.get(listenerBean.getMessageSource());
                        if (messageSource == null &&
                                messageSourceMap.size() == 1 &&
                                StringUtils.isEmpty(listenerBean.getMessageSource())) {
                            messageSource = messageSourceMap.entrySet().iterator().next().getValue();
                        }
                        String depend = resolveValue(listenerBean.getDepend());
                        String chooser = resolveValue(listenerBean.getChooser());
                        String topic = resolveValue(listenerBean.getTopic());

                        MessageModel messageModel = listenerBean.getMessageModel();

                        logger.info("开始注册Listener,depend:{},chooser:{},topic:{},messageMode:{}",depend,chooser,topic,messageModel);
                        Terminal terminal = messageSource.getTerminal(
                                new Subject().setLabel(chooser)
                                        .setTopic(topic)
                                        .setMessageModel(listenerBean.getMessageModel()));
                        terminal.subscribe(
                                depend,
                                new com.svnyoung.pubsub.Listener<Serializable>() {
                                    @Override
                                    public Reply onMsg(Message<Serializable> message) {
                                        return messageHandler.onMsg(message.getContent());
                                    }
                                    @Override
                                    public Class<Serializable> getMessageClass() {
                                        return (Class<Serializable>) messageHandler.getMessageClass();
                                    }
                                });
                        logger.info("注册Listener成功,depend:{},chooser:{},topic:{},messageMode:{}",depend,chooser,topic,messageModel);
                        }

                });
            }
        }
    }

    private String resolveValue(String key){

        if(beanFactory == null) {
            return null;
        }
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory)beanFactory;
        String value = configurableBeanFactory.resolveEmbeddedValue(key);
        if(value == null){
            return null;
        }
        BeanExpressionResolver beanExpressionResolver = configurableBeanFactory.getBeanExpressionResolver();

        Object evalValue = beanExpressionResolver.evaluate(value,new BeanExpressionContext(configurableBeanFactory, null));

        if(evalValue == null){
            return null;
        }
        return String.valueOf(evalValue);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!this.nonAnnotatedClasses.contains(bean.getClass())) {
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            Map<Method, Listeners> annotatedMethods = MethodIntrospector.selectMethods(targetClass,
                    (Method method) ->
                            AnnotationUtils.findAnnotation(method, Listeners.class)
            );
            if (annotatedMethods.isEmpty()) {
                this.nonAnnotatedClasses.add(bean.getClass());
                if (logger.isTraceEnabled()) {
                    logger.trace("No @Listeners annotations found on bean type: " + bean.getClass());
                }
            } else {
                // Non-empty set of methods
                for (Map.Entry<Method, Listeners> entry : annotatedMethods.entrySet()) {
                    processMQListener(entry.getValue(), entry.getKey(), bean, beanName);
                }
                if (logger.isDebugEnabled()) {
                    logger.debug(annotatedMethods.size() + " @Listeners methods processed on bean '"
                            + beanName + "': " + annotatedMethods);
                }
            }
        }
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
