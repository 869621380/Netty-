package org.example.Model.message.requestMessage;

import lombok.Data;

@Data
public abstract class GroupChatRequestMessage extends ChatRequestMessage {
    //群组ID
    protected String groupName;

    public GroupChatRequestMessage(String sendTime, Integer senderID, String groupName) {
        super(sendTime, senderID);
        this.groupName = groupName;
    }

    public GroupChatRequestMessage(String groupName) {
    }
}
