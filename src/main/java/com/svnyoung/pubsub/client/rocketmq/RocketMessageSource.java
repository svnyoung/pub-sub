package com.svnyoung.pubsub.client.rocketmq;

import com.svnyoung.pubsub.AbstractMessageSource;
import com.svnyoung.pubsub.Subject;
import com.svnyoung.pubsub.Terminal;
import com.svnyoung.pubsub.utils.RemotingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: sunyang
 * @date: 2019/8/15 10:04
 * @version: 1.0
 * @since: 1.0
 * @see:
 */
public class RocketMessageSource extends AbstractMessageSource {

    protected  static final Logger logger= LoggerFactory.getLogger(RocketMessageSource.class);

    private String clientIP = RemotingUtil.getLocalAddress();

    /**地址**/
    private String namesrvAddr;

    /**实例名**/
    private String instanceName = clientIP+"#consumer"+"@"+ RemotingUtil.getPid();

    /**最大消费线程数**/
    private int consumerThreadMax;

    /**最小消费线程数**/
    private int consumerThreadMin;

    /**发布超时时间**/
    private int sendTimeout;


    @Override
    protected Terminal doGetTerminal(Subject subject) {
        return new RocketTerminal(this,subject);
    }

    @Override
    public void init() throws Exception {

    }

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public int getConsumerThreadMax() {
        return consumerThreadMax;
    }

    public void setConsumerThreadMax(int consumerThreadMax) {
        this.consumerThreadMax = consumerThreadMax;
    }

    public int getConsumerThreadMin() {
        return consumerThreadMin;
    }

    public void setConsumerThreadMin(int consumerThreadMin) {
        this.consumerThreadMin = consumerThreadMin;
    }

    public int getSendTimeout() {
        return sendTimeout;
    }

    public void setSendTimeout(int sendTimeout) {
        this.sendTimeout = sendTimeout;
    }

}
