package org.example.Model.message;

//未实现
public class SingleFileChatMessage extends SingleChatChatMessage {

    @Override
    public int getMessageType() {
        return SingleFileRequestMessage;
    }
}
