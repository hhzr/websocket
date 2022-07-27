package com.sjx.websocket_demo.util.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArrayList;

@ServerEndpoint("/avboy")
@Component
public class WebSocket {

    private Session session;

    private static CopyOnWriteArrayList<WebSocket> webSockets=new CopyOnWriteArrayList<>();

    @OnOpen
    public void onOpen(Session session){
        this.session=session;
        webSockets.add(this);
        System.out.println("连上了一个，现在总共有："+webSockets.size()+"个连接");
    }

    @OnClose
    public void onClose(){
        webSockets.remove(this);
        System.out.println("关闭了一个，现在总共有："+webSockets.size()+"个连接");
    }

    @OnMessage
    public void onMessage(String message){

        for (WebSocket webSocket:webSockets){
            try {
                webSocket.session.getBasicRemote().sendText(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Throwable throwable){
        System.out.println("error");
    }

}
