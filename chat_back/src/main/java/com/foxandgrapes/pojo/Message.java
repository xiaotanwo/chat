package com.foxandgrapes.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

// 消息类
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Message {

    /**
     * 消息的类型：
     * 0-9：聊天室，0：聊天室列表，1：聊天信息
     * 10-19：群聊，10：群聊列表，11：群聊成员上线通知，12：群聊聊天信息
     * 20-29：好友，20：好友列表，21：在线好友列表，22：好友上线通知，23：好友填料信息
     */
    private Integer type;

    /**
     * 发送者
     */
    private String fromName;

    /**
     * 发送的对象
     */
    private String toName;

    /**
     * 携带的信息（消息，好友列表等）
     */
    private Object obj;
}
