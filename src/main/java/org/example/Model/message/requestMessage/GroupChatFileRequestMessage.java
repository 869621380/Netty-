package org.example.Model.message.requestMessage;

public class GroupChatFileRequestMessage extends GroupChatRequestMessage {

    @Override
    public int getMessageType() {
        return GroupFileMessage;
    }

}
