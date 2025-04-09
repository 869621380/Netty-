package org.example.Model.message;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class ChatMessage extends Message {
    //消息类型
    protected String ChatMessageType;
    //消息内容
    protected String content;
    //发送时间
    protected String sendTime;
    //发送状态
    protected String sendStatus;
    //发送人ID
    protected Integer senderID;


    private static final Map<Integer, Class<? extends ChatMessage>> messageClasses = new HashMap<>();


}
