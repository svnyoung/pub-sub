package com.svnyoung.pubsub;

import com.svnyoung.pubsub.message.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: sunyang
 * @date: 2019/8/20 11:12
 * @version: 1.0
 * @since: 1.0
 * @see:
 */
public abstract class AbstractMessageSource implements MessageSource{

    protected  static final Logger logger= LoggerFactory.getLogger(AbstractMessageSource.class);


    private Serializer serializer;


    private Map<Subject,Terminal> subjectTerminalMap = new ConcurrentHashMap<>();

    @Override
    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public Serializer getSerializer() {
        return serializer;
    }

    @Override
    public Terminal getTerminal(Subject subject) {
        Terminal terminal;
        if((terminal = subjectTerminalMap.get(subject)) == null){
            terminal = doGetTerminal(subject);
            subjectTerminalMap.put(subject,terminal);
        }
        return terminal;
    }

    @Override
    public Terminal getTerminal(String topic,String chooser) {
        return getTerminal(new Subject()
                .setTopic(topic)
                .setChooser(chooser)
                .setMessageModel(MessageModel.POINT_TO_POINT));
    }

    /**
     * 获取终端
     * @date 2019/8/20 11:14
     * @param subject 内容
     * @return 
     * @throws 
     */
    protected abstract Terminal doGetTerminal(Subject subject);

    @Override
    public void destroy() throws Exception{
        subjectTerminalMap.forEach((k,v)->{
            try{
                v.close();
            }catch (Exception e){
                logger.error("destroy messageSource:{} error",k,e);
            }
        });
    }

}
