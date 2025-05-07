package org.example.Model.message.requestMessage;

public class GroupChatVideoRequestMessage extends GroupChatRequestMessage {
    public GroupChatVideoRequestMessage(String sendTime, Integer senderID, String groupName) {
        super(sendTime, senderID, groupName);
    }

    @Override
    public int getMessageType() {
        return GroupVideoMessage;
    }

}
