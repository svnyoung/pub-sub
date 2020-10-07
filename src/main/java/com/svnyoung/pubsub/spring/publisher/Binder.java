package com.svnyoung.pubsub.spring.publisher;

public @interface Binder {


    /**
     * topic
     */
    String topic();


    String label() default "";

    /**
     * message源
     */
    String messageSource() default "autoMessageSource";

}
