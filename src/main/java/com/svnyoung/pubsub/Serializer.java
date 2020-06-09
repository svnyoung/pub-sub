package com.svnyoung.pubsub;

import com.svnyoung.pubsub.exception.SerializationException;

/**
 * 序列化器
 * @author sunyang
 * @date 2019/8/9 17:29
 * @version: 1.0
 * @since: 1.0
 **/
public interface Serializer {

    /**
     * 序列化
     * @date 2019/8/9 17:30
     * @param object 将序列化对象
     * @return
     * @throws SerializationException 序列化失败
     */
    <T> byte[] serialize(T object) throws SerializationException;

    
    /**
     * 反序列化
     * @date 2019/8/9 17:31
     * @param bytes 内容
     * @param tClass  反序列化对象
     * @return 
     * @throws SerializationException
     */
    <T> T deserialize(byte[] bytes, Class<T> tClass) throws SerializationException;

}
