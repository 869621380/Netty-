package org.example.Model.Domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GroupChatMessage extends Message {

    // 发送时间
    //private String sendTime;

    // 发送人ID
    //private Integer senderID;

    // 群聊名
    private String groupName;
    // 消息类型
    //private String type;

    // 消息内容 (text 或 byte[])
   // private Object content;


    // 构造函数                                                       -1用于标识
    public GroupChatMessage(String sendTime, String sendStatus, Integer senderID,String groupName, String type, Object content) {
        this.sendTime = sendTime;
        this.sendStatus = sendStatus;
        this.senderID = senderID;
        this.groupName=groupName;
        this.type = type;
        this.content = content;
    }
}
