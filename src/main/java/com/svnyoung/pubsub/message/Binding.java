package com.svnyoung.pubsub.message;

public @interface Binding {

    /**
     * topic
     * */
    String topic ();


    /**消息类型**/
    MessageModel messageModel () default MessageModel.POINT_TO_POINT;


    /**
     * 消息选择器
     * **/
    String chooser () default "";

}
