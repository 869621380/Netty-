package org.example.Model.message;

public class GroupFileChatMessage extends GroupChatChatMessage {

    @Override
    public int getMessageType() {
        return GroupFileMessage;
    }

}
