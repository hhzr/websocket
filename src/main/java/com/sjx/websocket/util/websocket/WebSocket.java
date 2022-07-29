package com.sjx.websocket.util.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@ServerEndpoint("/socketService/toChatWith/{nickname}")
@Component
@Slf4j
public class WebSocket {

    private Session session;

    private static final CopyOnWriteArrayList<WebSocket> WEB_SOCKETS =new CopyOnWriteArrayList<>();

    private static final Map<String, WebSocket> USERS = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "nickname") String nickname) throws IOException {
        this.session=session;
        WEB_SOCKETS.add(this);
        if (!StringUtils.isEmpty(nickname)) {
            USERS.put(nickname, this);
//            this.onMessage("<h5><strong>" + nickname + " : 加入群聊</strong></h5>", nickname);
        }
        log.info("连上了一个，现在总共有：{}个连接", USERS.size());
    }

    @OnClose
    public void onClose(@PathParam("nickname")String nickname){
        WEB_SOCKETS.remove(this);
        USERS.remove(nickname);
        System.out.println("关闭了一个，现在总共有："+ USERS.size()+"个连接");
    }

    @OnMessage
    public void onMessage(String message, @PathParam(value = "nickname")String nickname) throws IOException {
        USERS.forEach((k, v) -> {
            if (!k.equals(nickname)) {
                try {
                    v.session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnError
    public void onError(Throwable throwable){
        System.out.println("error");
    }

    public void onMessageToUser(String sendNickname, String nickname, String message) throws IOException {
        WebSocket WebSocket = USERS.get(nickname);
        if (WebSocket != null && WebSocket.session.isOpen()) {
            WebSocket.session.getBasicRemote().sendText("<h3><strong>" + sendNickname + "给你发来消息 : " + message + "</strong></h3>");
            WebSocket sendWebSocket = USERS.get(sendNickname);
            sendWebSocket.session.getBasicRemote().sendText("<h3><strong>您给" + nickname + "发送消息 : " + message + "</strong></h3>");
        } else {
            WebSocket = USERS.get(sendNickname);
            WebSocket.session.getBasicRemote().sendText("<h3><strong>" + nickname + "不存在或已下线</strong></h3>");
        }
    }

    public Set<String> getAllUser(String nickname) {
        Set<String> allUsers = USERS.keySet();
        allUsers.remove(nickname);
        return allUsers;
    }
}
