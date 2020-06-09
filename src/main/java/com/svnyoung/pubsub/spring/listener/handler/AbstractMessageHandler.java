package com.svnyoung.pubsub.spring.listener.handler;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author: sunyang
 * @date: 2019/8/16 19:47
 * @version: 1.0
 * @since: 1.0
 * @see:
 */
public abstract class AbstractMessageHandler<T extends Serializable> implements MessageHandler<T>{

    @Override
    public Class<T> getMessageClass() {
        Type type = this.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) type).getActualTypeArguments();
        Assert.isTrue(ArrayUtils.isNotEmpty(params) && params.length == 1,"泛型必须只有一个");
        Class<?> classT = (Class<?>)params[0];
        return  (Class<T>)classT;
    }

}
