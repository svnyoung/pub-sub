package com.svnyoung.pubsub;

/**
 *
 * message源
 * @author: sunyang
 * @date: 2019/8/3 10:06
 * @version: 1.0
 * @since: 1.0
 * @see:
 */
public interface MessageSource {


    /**
     * 设置序列化器
     * @date 2019/8/9 17:34
     * @param serializer 序列化器
     * @return
     * @throws
     */
    void setSerializer(Serializer serializer);


    /**
     *
     * 获取序列化器
     * @date 2019/8/9 17:34
     * @param 
     * @return 
     * @throws 
     */
    Serializer getSerializer();


    /**
     * 获取jms终端
     * @date 2019/8/9 17:49
     * @param subject
     * @return
     * @throws
     */
    Terminal getTerminal(Subject subject);



    /**
     * 获取jms终端
     * @date 2019/8/9 17:49
     * @param topic
     * @return
     * @throws
     */
    Terminal getTerminal(String topic,String chooser);


    /**
     * 销毁
     * @date 2019/8/15 10:22
     * @param
     * @return
     * @throws Exception
     */
    void destroy() throws Exception;



    /**
     * 初始化
     * @date 2019/8/20 11:49
     * @param
     * @return
     * @throws Exception
     */
    void init() throws Exception;

}
