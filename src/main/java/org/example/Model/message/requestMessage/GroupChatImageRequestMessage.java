package org.example.Model.message.requestMessage;

public class GroupChatImageRequestMessage extends GroupChatRequestMessage {

    byte[] content;

    public GroupChatImageRequestMessage(String sendTime, Integer senderID, String groupName,byte[] content) {
        super(sendTime, senderID, groupName);
        this.content=content;
    }

    @Override
    public int getMessageType() {
        return GroupImageMessage;
    }
}
