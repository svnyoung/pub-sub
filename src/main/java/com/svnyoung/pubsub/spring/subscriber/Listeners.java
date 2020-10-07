package com.svnyoung.pubsub.spring.subscriber;

import java.lang.annotation.*;

/**
 * @author: sunyang
 * @date: 2019/8/16 9:38
 * @version: 1.0
 * @since: 1.0
 * @see:
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Listeners {

    Listener[] value();

}
