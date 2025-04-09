package org.example.Model.message;

public class GroupVideoChatMessage extends GroupChatChatMessage {
    @Override
    public int getMessageType() {
        return GroupVideoMessage;
    }

}
