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
     * 消息的类型：0：系统，1：好友，2：群聊，3：聊天室
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
     * 发送的消息
     */
    private String message;

}
