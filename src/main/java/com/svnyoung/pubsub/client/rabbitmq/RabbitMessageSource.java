package com.svnyoung.pubsub.client.rabbitmq;

import com.rabbitmq.client.ConnectionFactory;
import com.svnyoung.pubsub.AbstractMessageSource;
import com.svnyoung.pubsub.Subject;
import com.svnyoung.pubsub.Terminal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: sunyang
 * @date: 2019/8/15 10:04
 * @version: 1.0
 * @since: 1.0
 * @see:
 */
public class RabbitMessageSource extends AbstractMessageSource {

    protected  static final Logger logger= LoggerFactory.getLogger(RabbitMessageSource.class);

    /**地址**/
    private String namesrvAddr;

    private String username;

    private String password;

    private int connectTimeout;

    private ConnectionFactory connectionFactory;


    @Override
    protected Terminal doGetTerminal(Subject subject) {
        return new RabbitTerminal(this,subject);
    }


    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    @Override
    public void init() throws Exception {
        connectionFactory = new ConnectionFactory();
    }
}
