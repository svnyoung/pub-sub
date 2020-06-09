package com.svnyoung.pubsub.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @Author ：sunyang
 * @Date ：2018/11/30 10:38
 * @Description：
 * @Modified By：
 * @Version: 1.0
 */
@SpringBootApplication
@ImportResource(locations = {"classpath*:spring/message-test.xml"})
public class Application {

        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }
    }

