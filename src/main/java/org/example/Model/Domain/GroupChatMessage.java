package org.example.Model.Domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GroupChatMessage extends Message {

    // 发送时间
    private String sendTime;

    // 发送人ID
    private Integer senderID;

    // 群聊ID
    private Integer groupID;

    // 接收者ID列表（多个接收者）
    private List<Integer> receiverIDs;

    // 消息类型
    private String type;

    // 消息内容 (text 或 byte[])
    private Object content;

    // 构造函数
    public GroupChatMessage(String sendTime, String sendStatus, Integer senderID, Integer groupID, List<Integer> receiverIDs, String type, Object content) {
        this.sendTime = sendTime;
        this.sendStatus = sendStatus;
        this.senderID = senderID;
        this.groupID = groupID;
        this.receiverIDs = receiverIDs;
        this.type = type;
        this.content = content;
    }
}
