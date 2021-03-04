package com.foxandgrapes.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    public static Map<String, ChatEndpoint> getOnlineUsers() { return onlineUsers; }

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
    private static String[] chatRooms = new String[] {"北京", "上海", "广州", "深圳"};

    // 声明Session对象，通过该对象可以发送消息给指定的用户
    private Session session;
    public Session getSession() { return session; }

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
        if (userName == null) {
            onClose();
            return;
        }

        // 将当前对象存储到容器中
        onlineUsers.put(userName, this);

        // 推送聊天室
        sendChatRooms();
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

    private void sendChatRooms() {
        Message message = new Message();
        message.setType(0);
        message.setObj(chatRooms);
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
        try {
            // 心跳
            if ("ping".equals(message)) {
                session.getBasicRemote().sendText("pang");
                return;
            }
            String userName = (String) httpSession.getAttribute("user");
            ObjectMapper objectMapper = new ObjectMapper();
            Message msg = objectMapper.readValue(message, Message.class);
            msg.setFromName(userName);
            switch (msg.getType()) {
                case 0:
                    // 聊天室消息
                    for (String chatRoom : chatRooms) {
                        if (chatRoom.equals(msg.getToName())) {
                            msg.setType(1);
                            for (Map.Entry<String, ChatEndpoint> entry : onlineUsers.entrySet()) {
                                // 不对自己进行广播
                                if (!userName.equals(entry.getKey())) {
                                    entry.getValue().session.getBasicRemote().sendText(MessageUtils.getMessage(msg));
                                }
                            }
                            break;
                        }
                    }
                    break;
                case 1:
                    // 群聊消息
                    msg.setType(12);
                    if (groupOnlineUsers.containsKey(msg.getToName())) {
                        for (String user : groupOnlineUsers.get(msg.getToName())) {
                            if (!userName.equals(user)) {
                                onlineUsers.get(user).session.getBasicRemote().sendText(MessageUtils.getMessage(msg));
                            }
                        }
                    }
                    break;
                case 2:
                    // 好友消息
                    if (onlineFriends.get(userName).contains(msg.getToName())) {
                        // 在线
                        msg.setType(23);
                        ChatEndpoint chatEndpoint = onlineUsers.get(msg.getToName());
                        chatEndpoint.session.getBasicRemote().sendText(MessageUtils.getMessage(msg));
                    } else {
                        // 已离线
                        msg.setType(24);
                        session.getBasicRemote().sendText(MessageUtils.getMessage(msg));
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose() {
        try {
            String userName = (String) httpSession.getAttribute("user");
            if (userName == null) return;
            Message message = new Message();
            message.setFromName(userName);

            // 移除在线好友的好友，并通知好友
            List<String> friends =  onlineFriends.get(userName);
            for (String friend : friends) {
                onlineFriends.get(friend).remove(userName);
                message.setType(25);
                onlineUsers.get(friend).session.getBasicRemote().sendText(MessageUtils.getMessage(message));
            }
            onlineFriends.remove(userName);
            // 并通知群友已下线
            List<String> groups = allGroups.get(userName);
            message.setType(13);
            for (String group : groups) {
                message.setToName(group);
                List<String> users = groupOnlineUsers.get(group);
                for (String user : users) {
                    if (user.equals(userName)) continue;
                    onlineUsers.get(user).session.getBasicRemote().sendText(MessageUtils.getMessage(message));
                }
                users.remove(userName);
                if (users.size() == 0) {
                    // 移除在线群聊人数为0的群聊
                    groupOnlineUsers.remove(group);
                }
            }

            // 移除在线用户
            onlineUsers.remove(userName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
