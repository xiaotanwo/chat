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

    // 保存每一个群聊中的在线用户
    private static Map<String, List<String>> groupOnlineUsers = new ConcurrentHashMap<>();
    public static Map<String, List<String>> getGroupOnlineUsers() { return groupOnlineUsers; }

    // 聊天室
    private static String[] chatRoom = new String[] {"北京", "上海", "广州", "深圳"};

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

        // 推送聊天室
        sendChatRoom();
        // 推送全部好友信息
        sendAllFriends(userName);
        // 推送在线好友信息
        sendOnlineFriends(userName);
        // 推送群聊信息
        sendAllGroup(userName);
        // 通知好友(上线通知)
        broadcastOnlineFriends(userName);
        // 通知群聊（上线通知）
        broadcastGroups(userName);

    }

    private void sendChatRoom() {
        Message message = new Message();
        message.setType(0);
        message.setObj(chatRoom);
        try {
            session.getBasicRemote().sendText(MessageUtils.getMessage(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcastGroups(String userName) {
        List<String> groups = allGroups.get(userName);
        Message message = new Message();
        message.setType(11);
        message.setFromName(userName);
        try {
            for (String group : groups) {
                // 待续
                if (!groupOnlineUsers.containsKey(group)) {
                    groupOnlineUsers.put(group, new ArrayList<>());
                }
                List<String> groupUsers = groupOnlineUsers.get(group);
                for (String user : groupUsers) {
                    // 不对自己行广播
                    if (user.equals(userName)) continue;
                    message.setToName(group);
                    ChatEndpoint chatEndpoint = onlineUsers.get(user);
                    chatEndpoint.session.getBasicRemote().sendText(MessageUtils.getMessage(message));
                }
                // 添加该用户
                groupUsers.add(userName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void broadcastOnlineFriends(String userName) {
        List<String> friends = onlineFriends.get(userName);
        Message message = new Message();
        message.setType(22);
        // 上线通知，来自XXX
        message.setFromName(userName);
        try {
            for (String friend : friends) {
                if (friend.equals(userName)) continue;
                ChatEndpoint chatEndpoint = onlineUsers.get(friend);
                chatEndpoint.session.getBasicRemote().sendText(MessageUtils.getMessage(message));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendAllGroup(String userName) {
        Message message = new Message();
        message.setType(10);
        message.setObj(allGroups.get(userName));
        try {
            session.getBasicRemote().sendText(MessageUtils.getMessage(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendOnlineFriends(String userName) {
        Message message = new Message();
        message.setType(21);
        message.setObj(onlineFriends.get(userName));
        try {
            session.getBasicRemote().sendText(MessageUtils.getMessage(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendAllFriends(String userName) {
        Message message = new Message();
        message.setType(20);
        message.setObj(getFriendVoList(userName));
        try {
            session.getBasicRemote().sendText(MessageUtils.getMessage(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Object> getFriendVoList(String userName) {
        List<Object> list = new ArrayList<>();
        List<String> onlineFriendList = new ArrayList<>();
        for (String friend : allFriends.get(userName)) {
            // 好友在线
            if (onlineUsers.containsKey(friend)) {
                // 添加在线好友
                onlineFriendList.add(friend);
                // 添加在线好友的在线好友
                onlineFriends.get(friend).add(userName);
            }
            list.add(new FriendVo(friend));
        }
        // 保存在线的好友列表
        onlineFriends.put(userName, onlineFriendList);
        return list;
    }

    @OnMessage
    public void onMessage(String message, Session session) {

    }

    @OnClose
    public void onClose(Session session) {

    }
}
