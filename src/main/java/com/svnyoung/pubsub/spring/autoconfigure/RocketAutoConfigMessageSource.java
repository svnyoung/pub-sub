package com.svnyoung.pubsub.spring.autoconfigure;

import com.svnyoung.pubsub.Serializer;
import com.svnyoung.pubsub.client.rocketmq.RocketMessageSource;
import com.svnyoung.pubsub.serialize.KryoSerializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;

@ConditionalOnClass(name = {"org.apache.rocketmq.client.ClientConfig"})
public class RocketAutoConfigMessageSource extends RocketMessageSource {

        /**
         * 地址
         **/
        @Value("${message.rocket.namesrvAddr}")
        private String namesrvAddr;

        /**
         * 实例名
         **/
        @Value("${message.rocket.instanceName:}")
        private String instanceName;

        /**
         * 最大消费线程数
         **/
        @Value("${message.rocket.consumerThreadMax:20}")
        private int consumerThreadMax;

        /**
         * 最小消费线程数
         **/
        @Value("${message.rocket.consumerThreadMin:10}")
        private int consumerThreadMin;

        /**
         * 发布超时时间
         **/
        @Value("${message.rocket.sendTimeout:5000}")
        private int sendTimeout;


        @Value("${message.rocket.serializer:com.svnyoung.pubsub.serialize.FastJsonSerializer}")
        private Class<Serializer> serializer;

        @Override
        public String getNamesrvAddr() {
            return namesrvAddr;
        }

        @Override
        public String getInstanceName() {
            if(StringUtils.isBlank(instanceName)){
                return super.getInstanceName();
            }
            return instanceName;
        }

        @Override
        public int getConsumerThreadMax() {
            return consumerThreadMax;
        }

        @Override
        public int getConsumerThreadMin() {
            return consumerThreadMin;
        }

        @Override
        public int getSendTimeout() {
            return sendTimeout;
        }

        @Override
        public Serializer getSerializer() {
            return serializer != null ? BeanUtils.instantiateClass(serializer) : new KryoSerializer();
        }
}