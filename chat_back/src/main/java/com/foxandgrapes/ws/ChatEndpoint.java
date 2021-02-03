package com.foxandgrapes.ws;

import com.foxandgrapes.pojo.Message;
import com.foxandgrapes.utils.MessageUtils;
import com.foxandgrapes.vo.FriendVo;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat", configurator = GetHttpSessionConfigurator.class)
@Component
public class ChatEndpoint {

    // 用来存储每一个客户端对象对应的ChatEndpoint对象
    private static Map<String, ChatEndpoint> onlineUsers = new ConcurrentHashMap<>();

    // 保存每一个用户的所有好友
    private static Map<String, List<String>> allFriends = new ConcurrentHashMap<>();
    public static Map<String, List<String>> getAllFriends() { return allFriends; }

    // 保存每一个用户的在线好友
    private static Map<String, List<String>> onlineFriends = new ConcurrentHashMap<>();
    public static Map<String, List<String>> getOnlineFriends() { return onlineFriends; }

    // 保存每一个用户的所有群聊
    private static Map<String, List<String>> allGroups = new ConcurrentHashMap<>();
    public static Map<String, List<String>> getAllGroups() { return allGroups; }

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

        Message message = null;
        try {
            // 推送全部好友信息
            message = new Message();
            message.setIsSystem(true);
            message.setType(2);
            message.setObj(getFriendVoList(userName));
            session.getBasicRemote().sendText(MessageUtils.getMessage(message));

            // 推送群聊信息
            message.setType(1);
            message.setObj(allGroups.get(userName));
            session.getBasicRemote().sendText(MessageUtils.getMessage(message));

            // 推送在线好友信息
            message.setType(3);
            message.setObj(onlineFriends.get(userName));
            session.getBasicRemote().sendText(MessageUtils.getMessage(message));

        } catch (IOException e) {
            e.printStackTrace();
        }

        // sendOnlineFriends();
        //
        // broadcastAllFriends();
        //
        // broadcastAllGroups();

        // 将当前在线用户的用户名推送给所有的客户端
        // 1.获取消息
        // String msg = MessageUtils.getMessage(getNames());
        // // 2.调用方法进行系统消息的推送
        // broadcastAllUsers(msg);
    }

    private List<Object> getFriendVoList(String userName) {
        List<Object> list = new ArrayList<>();
        List<String> onlineFriendList = new ArrayList<>();
        for (String friend : allFriends.get(userName)) {
            if (onlineUsers.containsKey(friend)) {
                onlineFriendList.add(friend);
            }
            list.add(new FriendVo(friend));
        }
        // 保存在线的好友列表
        onlineFriends.put(userName, onlineFriendList);
        return list;
    }

    // private void broadcastAllUsers(String message) {
    //     try {
    //         // 要将该消息推送给所有的客户端
    //         Set<String> names = getNames();
    //         for (String name : names) {
    //             ChatEndpoint chatEndpoint = onlineUsers.get(name);
    //             chatEndpoint.session.getBasicRemote().sendText(message);
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    // 获取在线好友,待完善
    // private Set<String> getNames() {
    //     return onlineUsers.keySet();
    // }

    @OnMessage
    public void onMessage(String message, Session session) {

    }

    @OnClose
    public void onClose(Session session) {

    }
}
