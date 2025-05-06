package org.example.Model.Domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SingleChatMessage extends Message{

    //发送时间
    //private String sendTime;

    //发送人ID
    //private Integer senderID;
    //接收人
    private Integer receiverID;
    //消息类型
    //private String type;
    //消息内容
    //private Object content;



    public SingleChatMessage(String sendTime, String sendStatus, Integer senderID, Integer receiverID, String type, Object content) {
        this.sendTime = sendTime;
        this.sendStatus = sendStatus;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.type = type;
        this.content = content;
    }

}
