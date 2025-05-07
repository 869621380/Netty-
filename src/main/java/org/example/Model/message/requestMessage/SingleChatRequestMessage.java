package org.example.Model.message.requestMessage;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class SingleChatRequestMessage extends ChatRequestMessage {
    protected Integer receiverID;

    SingleChatRequestMessage(String sendTime, Integer senderID, Integer receiverID) {
        super(sendTime,senderID);
        this.receiverID = receiverID;
    }

}
