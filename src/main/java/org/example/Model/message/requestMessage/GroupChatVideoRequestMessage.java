package org.example.Model.message.requestMessage;

public class GroupChatVideoRequestMessage extends GroupChatRequestMessage {
    @Override
    public int getMessageType() {
        return GroupVideoMessage;
    }

}
