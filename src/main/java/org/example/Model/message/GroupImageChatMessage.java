package org.example.Model.message;

public class GroupImageChatMessage extends GroupChatChatMessage {
    @Override
    public int getMessageType() {
        return GroupImageMessage;
    }
}
