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
     * true: 系统信息，false：聊天信息
     */
    private Boolean isSystem;

    /**
     * 消息的类型：0：聊天室，1：群聊，2：好友，
     * 系统消息外加类型：3：在线好友
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
     * 发送的消息 / 推送的系统信息
     */
    private String message;

    /**
     * 返回的列表（聊天室，群聊，好友）
     */
    private Object obj;
}
