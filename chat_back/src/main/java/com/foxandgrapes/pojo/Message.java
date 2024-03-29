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
     * 0-9：聊天室，0：聊天室列表，1：聊天信息，2：聊天图片
     * 10-19：群聊，10：群聊列表，11：群聊成员上线通知，12：群聊聊天信息，13：群友离线通知，14：用户加入群聊通知，15：用户退出群聊通知，16：群聊图片
     * 20-29：好友，20：好友列表，21：在线好友列表，22：好友上线通知，23：好友聊天信息，24：好友离线反馈，25：好友离线通知，
     *              26：好友申请通知，27：好友申请通过通知，28：好友删除通知，29：私聊图片
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
