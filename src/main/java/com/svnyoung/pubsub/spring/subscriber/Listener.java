package com.svnyoung.pubsub.spring.subscriber;

import com.svnyoung.pubsub.message.MessageModel;

import java.lang.annotation.*;

/**
 * @author ：sunyang
 * @date ：2018/12/10 19:21
 * @description：
 * @modified By：
 * @version: 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Listener {
    /**
     * message源
     */
    String messageSource() default "autoMessageSource";

    /**
     * 分组
     */
    String group() default "";

    /**
     * topic
     */
    String topic();

    /**
     * 选择器
     * **/
    String chooser() default "";

    /**
     * 消息类型
     * **/
    MessageModel messageModel() default MessageModel.POINT_TO_POINT;
}
