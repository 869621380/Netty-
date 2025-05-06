package org.example.Model.message.requestMessage;

public class GroupChatTextRequestMessage extends GroupChatRequestMessage {
    //消息内容
    protected String content;


    public GroupChatTextRequestMessage(String sendTime, Integer senderID, String receiverName, String content){
        super(sendTime,senderID,receiverName);
        this.content = content;
    }
    @Override
    public int getMessageType() {
        return GroupTextMessage;
    }
}
