package org.example.Model.message.requestMessage;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class GroupJoinRequestMessage extends GroupChatRequestMessage{
    //private String groupName;
    private Integer username;

    public GroupJoinRequestMessage(Integer username, String groupName) {
        super(groupName);
        //this.groupName = groupName;
        this.username = username;
    }

    @Override
    public int getMessageType() {
        return GroupJoinMessage;
    }
}
