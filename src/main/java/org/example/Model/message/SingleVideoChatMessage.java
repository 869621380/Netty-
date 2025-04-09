package org.example.Model.message;


/*未实现*/
public  class SingleVideoChatMessage extends ChatMessage {

    @Override
    public int getMessageType() {
        return SingleVideoRequestMessage;
    }
}
