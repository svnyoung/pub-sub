package com.svnyoung.pubsub.spring.subscriber.handler;

import com.svnyoung.pubsub.exception.MessageHandlerException;
import com.svnyoung.pubsub.spring.subscriber.ListenerBean;
import com.svnyoung.pubsub.Reply;

import java.io.Serializable;
import java.util.List;

/**
 * @author ：sunyang
 * @date ：2018/12/10 18:59
 * @description：
 * @modified By：
 * @version: 1.0
 * @since 1.0
 */
public interface MessageHandler<T extends Serializable>  {

    /**
     * 执行消息
     * @date 2018/12/11 19:39
     * @param msg 消息内容
     * @return MsgReply 返回的结果
     * @see Reply
     * @throws MessageHandlerException
     */
    Reply onMsg(T msg) throws MessageHandlerException;

    /**
     * 支持的消息类型
     * @date 2018/12/11 19:41
     * @return
     * @throws
     */
    List<ListenerBean> support();


    /**
     * 获取消息类型
     * @date 2019/8/16 19:59
     * @param
     * @return
     * @throws
     */
    Class<T> getMessageClass();


}
