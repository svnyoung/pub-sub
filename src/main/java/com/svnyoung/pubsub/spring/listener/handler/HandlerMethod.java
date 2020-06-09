package com.svnyoung.pubsub.spring.listener.handler;

import com.svnyoung.pubsub.spring.autoconfigure.MessageAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author ：sunyang
 * @date ：2018/12/12 10:21
 * @description：
 * @modified By：
 * @version: 1.0
 */
public class HandlerMethod {

    protected  static final Logger logger= LoggerFactory.getLogger(MessageAutoConfiguration.class);

    private final Object bean;

    private final Class<?> beanType;

    private final Method method;

    private final Method bridgedMethod;

    public HandlerMethod(Object bean, Method method) {
        Assert.notNull(bean, "Bean is required");
        Assert.notNull(method, "Method is required");
        this.bean = bean;
        this.beanType = ClassUtils.getUserClass(bean);
        this.method = method;
        this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
    }

    public HandlerMethod(Object bean, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        Assert.notNull(bean, "Bean is required");
        Assert.notNull(methodName, "Method name is required");
        this.bean = bean;
        this.beanType = ClassUtils.getUserClass(bean);
        this.method = bean.getClass().getMethod(methodName, parameterTypes);
        this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(this.method);
    }

    public HandlerMethod(String beanName, BeanFactory beanFactory, Method method) {
        Assert.hasText(beanName, "Bean name is required");
        Assert.notNull(beanFactory, "BeanFactory is required");
        Assert.notNull(method, "Method is required");
        this.bean = beanName;
        this.beanType = ClassUtils.getUserClass(beanFactory.getType(beanName));
        this.method = method;
        this.bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
    }


    public Object doInvoke(Object ... args) throws Exception{
        ReflectionUtils.makeAccessible(getBridgedMethod());
        try {
            return getBridgedMethod().invoke(getBean(), args);
        }
        catch (IllegalArgumentException ex) {
            logger.error("invoke method:{},args:{}",getBridgedMethod().getName(),args);
            throw new IllegalStateException();
        }
        catch (InvocationTargetException ex) {
            Throwable targetException = ex.getTargetException();
            if (targetException instanceof RuntimeException) {
                throw (RuntimeException) targetException;
            }
            else if (targetException instanceof Error) {
                throw (Error) targetException;
            }
            else if (targetException instanceof Exception) {
                throw (Exception) targetException;
            }
            else {
                throw new IllegalStateException("Failed to invoke handler method", targetException);
            }
        }
    }


    public Object getBean() {
        return this.bean;
    }

    public Method getMethod() {
        return this.method;
    }

    public Class<?> getBeanType() {
        return this.beanType;
    }
    protected Method getBridgedMethod() {
        return this.bridgedMethod;
    }


    public <A extends Annotation> A getMethodAnnotation(Class<A> annotationType) {
        return AnnotatedElementUtils.findMergedAnnotation(this.method, annotationType);
    }

    public <A extends Annotation> boolean hasMethodAnnotation(Class<A> annotationType) {
        return AnnotatedElementUtils.hasAnnotation(this.method, annotationType);
    }
    public String getShortLogMessage() {
        int args = this.method.getParameterTypes().length;
        return getBeanType().getName() + "#" + this.method.getName() + "[" + args + " args]";
    }

}
