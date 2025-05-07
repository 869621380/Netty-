package org.example.Model.message.requestMessage;

import lombok.Data;
import lombok.NoArgsConstructor;

// 文本消息内容类
@Data
@NoArgsConstructor
public class SingleChatTextRequestMessage extends SingleChatRequestMessage {
    //消息内容
    protected String content;
    @Override
    public int getMessageType() {
        return SingleTextRequestMessage;
    }

    public SingleChatTextRequestMessage(String sendTime, Integer senderID, Integer receiverID, String content){
        super(sendTime,senderID,receiverID);
        this.content = content;
    }



}
