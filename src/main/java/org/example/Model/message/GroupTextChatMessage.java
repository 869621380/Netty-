package org.example.Model.message;

public class GroupTextChatMessage extends GroupChatChatMessage {
    @Override
    public int getMessageType() {
        return GroupTextMessage;
    }
}
