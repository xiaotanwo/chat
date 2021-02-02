package com.foxandgrapes.ws;

import com.foxandgrapes.utils.MessageUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat", configurator = GetHttpSessionConfigurator.class)
@Component
public class ChatEndpoint {

    // 用来存储每一个客户端对象对应的ChatEndpoint对象
    private static Map<String, ChatEndpoint> onlineUsers = new ConcurrentHashMap<>();

    // 声明Session对象，通过该对象可以发送消息给指定的用户
    private Session session;

    // 声明一个HttpSession对象，用来获取用户名
    private HttpSession httpSession;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        // 赋值
        this.session = session;
        // 获取HttpSession对象
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        this.httpSession = httpSession;
        // 获取用户名
        String userName = (String) httpSession.getAttribute("user");
        // 没登录则直接返回
        if (userName == null) return;
        // 将当前对象存储到容器中
        onlineUsers.put(userName, this);
        // 将当前在线用户的用户名推送给所有的客户端
        // 1.获取消息
        String message = MessageUtils.getMessage(getNames());
        // 2.调用方法进行系统消息的推送
        broadcastAllUsers(message);
    }

    private void broadcastAllUsers(String message) {
        try {
            // 要将该消息推送给所有的客户端
            Set<String> names = getNames();
            for (String name : names) {
                ChatEndpoint chatEndpoint = onlineUsers.get(name);
                chatEndpoint.session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取在线好友,待完善
    private Set<String> getNames() {
        return onlineUsers.keySet();
    }

    @OnMessage
    public void onMessage(String message, Session session) {

    }

    @OnClose
    public void onClose(Session session) {

    }
}
