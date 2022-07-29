package com.sjx.websocket.controller;

import com.sjx.websocket.pojo.Response;
import com.sjx.websocket.util.websocket.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping(value = "/join")
@Slf4j
public class TestController {

    @Resource
    private WebSocket webSocket;

    @GetMapping("/login")
    public void login(@RequestParam(value = "nickname")String nickname) {
//        webSocket.onMessage("<h5><strong>" + nickname + " : 加入群聊</strong></h5>");
    }

    @GetMapping("/getAllUser")
    public Response<Set<String>> getAllUser(@RequestParam(value = "nickname")String nickname) {
        Set<String> allUser = webSocket.getAllUser(nickname);
        return new Response<>(200, "成功", allUser);
    }

    @GetMapping("/sendMessageToUser")
    public Response<String> sendMessageToUser(@RequestParam(value = "sendNickname")String sendNickname,
                                              @RequestParam(value = "nickname")String nickname,
                                              @RequestParam(value = "message")String message) {
        try {
            webSocket.onMessageToUser(sendNickname, nickname, message);
            return new Response<>(200, "成功", null);
        } catch (IOException e) {
            log.error("发送失败", e);
            return new Response<>(5001, "失败", null);
        } catch (Exception e) {
            log.error("服务错误", e);
            return new Response<>(5000, "失败", null);
        }
    }
}
