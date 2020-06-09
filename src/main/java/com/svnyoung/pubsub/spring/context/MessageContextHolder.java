package com.svnyoung.pubsub.spring.context;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：sunyang
 * @date ：2018/12/4 14:15
 * @version: 1.0
 */
public class MessageContextHolder {


    /**
     * 内置对象map
     * **/
    private Map<Object,Object> inner = new HashMap<>();

    private static ThreadLocal<MessageContextHolder> STORE = ThreadLocal.withInitial(()->new MessageContextHolder());

    /**
     * 获取上下文
     * @date 2018/12/4 14:40
     * @return  InvokeStore
     */
    public static MessageContextHolder getHolder() {
        return STORE.get();
    }


    /**
     * 清除上下文对象
     * @date 2018/12/4 14:40
     * @return  HttpInvokeContext
     */
    public static void remove() {
        STORE.remove();
    }


    public void put(Object key, Object object) {
        this.inner.put(key, object);
    }

    public Object get(Object key) {
        return this.inner.get(key);
    }

    public void remove(String key) {
        this.inner.remove(key);
    }



}
