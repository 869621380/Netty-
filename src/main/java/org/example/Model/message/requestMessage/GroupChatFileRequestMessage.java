package org.example.Model.message.requestMessage;

public class GroupChatFileRequestMessage extends GroupChatRequestMessage {

    public GroupChatFileRequestMessage(String sendTime, Integer senderID, String groupName) {
        super(sendTime, senderID, groupName);
    }

    @Override
    public int getMessageType() {
        return GroupFileMessage;
    }

}
