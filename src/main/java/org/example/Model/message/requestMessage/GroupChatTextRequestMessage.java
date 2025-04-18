package org.example.Model.message.requestMessage;

public class GroupChatTextRequestMessage extends GroupChatRequestMessage {
    @Override
    public int getMessageType() {
        return GroupTextMessage;
    }
}
