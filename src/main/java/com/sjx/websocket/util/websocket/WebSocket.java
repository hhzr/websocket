package com.sjx.websocket.util.websocket;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sjx.websocket.entity.enums.GlobalConstants;
import com.sjx.websocket.entity.http.Response;
import com.sjx.websocket.entity.module.User;
import com.sjx.websocket.entity.vo.SendMessageVO;
import com.sjx.websocket.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
@Slf4j
@ServerEndpoint(value = "/socketService/toChatWith/{userId}", encoders = {SocketEncoder.class})
public class WebSocket {

    private Session session;

    private static ApplicationContext applicationContext;

    private static final CopyOnWriteArrayList<WebSocket> WEB_SOCKETS =new CopyOnWriteArrayList<>();

    private static final Map<String, WebSocket> USERS = new HashMap<>();

    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocket.applicationContext = applicationContext;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") Long userId) throws IOException {
        this.session=session;
        WEB_SOCKETS.add(this);
        UserMapper userMapper = applicationContext.getBean(UserMapper.class);
        User user = userMapper.selectById(userId);
        USERS.put(user.getUserId().toString(), this);
        log.info("连上了一个，现在总共有：{}个连接", USERS.size());
    }

    @OnClose
    public void onClose(@PathParam("userId")Long userId){
        WEB_SOCKETS.remove(this);
        UserMapper userMapper = applicationContext.getBean(UserMapper.class);
        User user = userMapper.selectById(userId);
        USERS.remove(user.getUserId().toString());
        System.out.println("关闭了一个，现在总共有："+ USERS.size()+"个连接");
    }

    @OnMessage
    public void onMessage(String message, @PathParam(value = "userId")Long userId) throws IOException {
        UserMapper userMapper = applicationContext.getBean(UserMapper.class);
        User user = userMapper.selectById(userId);
        USERS.forEach((k, v) -> {
            if (!k.equals(user.getUserId().toString())) {
                try {
                    SendMessageVO sendMessageVO = new SendMessageVO(user.getUserNickname(), user.getUserHeadImg(), message);
                    v.session.getBasicRemote().sendObject(sendMessageVO);
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnError
    public ResponseEntity<Response<String>> onError(Throwable throwable){
        log.error("websocket通讯异常", throwable);
        return ResponseEntity.ok(Response.error(GlobalConstants.WEB_SOCKET_ERR.getCode(), GlobalConstants.WEB_SOCKET_ERR.getMessage()));
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

    public List<User> getAllUser(Long userId) {
        List<Long> allUsers = USERS.keySet().stream().mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
        allUsers.remove(userId);
        UserMapper userMapper = applicationContext.getBean(UserMapper.class);
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.in("user_id", allUsers);
        return userMapper.selectList(queryWrapper);
    }
}
