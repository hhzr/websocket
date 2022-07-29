package com.sjx.websocket_demo.controller;

import com.sjx.websocket_demo.util.websocket.WebSocket;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping(value = "/join")
public class TestController {

    @Resource
    private WebSocket webSocket;

    @GetMapping("/ws")
    public void ws(@RequestParam(value = "nickname")String nickname) {
//        webSocket.onMessage("<h5><strong>" + nickname + " : 加入群聊</strong></h5>");
    }

}
