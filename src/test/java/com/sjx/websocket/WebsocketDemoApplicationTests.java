package com.sjx.websocket;

import com.sjx.websocket.controller.UserController;
import com.sjx.websocket.util.ip2region.IpAddressUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(value = SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebsocketDemoApplicationTests {

    @Resource
    private UserController userController;

    @Test
    public void contextLoads() {
//        userController.login("13363150950", "123456");
//        System.out.println(IpAddressUtil.getMacAddress());
    }

}