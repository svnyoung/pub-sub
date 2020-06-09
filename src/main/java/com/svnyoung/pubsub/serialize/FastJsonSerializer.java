
package com.svnyoung.pubsub.serialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.svnyoung.pubsub.Serializer;
import com.svnyoung.pubsub.exception.SerializationException;

/**
 *
 * 采用FastJson方式序列化
 * @author sunyang
 * @date 2019/8/15 10:00
 * @version: 1.0
 * @since: 1.0
 **/
public class FastJsonSerializer implements Serializer {
    public FastJsonSerializer() {
    }


    @Override
    public <T> byte[] serialize(T t) throws SerializationException {
        return JSON.toJSONBytes(t, new SerializerFeature[0]);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws SerializationException {
        return JSON.parseObject(bytes, clazz, new Feature[0]);
    }
}
