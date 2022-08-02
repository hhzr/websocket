package com.sjx.websocket;

import com.sjx.websocket.util.websocket.WebSocket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class WebsocketDemoApplication {

    public static void main(String[] args) {
//        SpringApplication.run(WebsocketDemoApplication.class, args);
        SpringApplication springApplication = new SpringApplication(WebsocketDemoApplication.class);
        ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
        //解决WebSocket不能注入的问题
        WebSocket.setApplicationContext(configurableApplicationContext);
    }

}
