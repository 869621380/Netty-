package org.example.Model.message.responseMessage;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class GroupChatTextResponseMessage extends RequestResponseMessage{
    private String sendTime;
    private Integer from;
    private String content;
    private String groupName;

    public GroupChatTextResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    public GroupChatTextResponseMessage(String sendTime,Integer from, String content,String groupName) {
        this.sendTime = sendTime;
        this.from = from;
        this.content = content;
        this.groupName = groupName;
    }

    @Override
    public int getMessageType() {
        return GroupTextResponseMessage;
    }
}
