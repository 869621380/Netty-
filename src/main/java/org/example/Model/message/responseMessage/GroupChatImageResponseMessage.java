package org.example.Model.message.responseMessage;


import lombok.Data;

@Data
public class GroupChatImageResponseMessage extends RequestResponseMessage{
    byte[] content;
    private String groupName;
    private Integer from;
    private String time;

    @Override
    public int getMessageType() {
        return GroupImageResponseMessage;
    }

    public GroupChatImageResponseMessage(String sendTime, Integer senderID, String groupName, byte[] content){
        this.time = sendTime;
        this.content = content;
        this.from = senderID;
        this.groupName = groupName;
    }
}