package org.example.Model.message.responseMessage;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class GroupJoinResponseMessage extends RequestResponseMessage{

    private Integer from;
    private String groupName;

    public GroupJoinResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    public GroupJoinResponseMessage(Integer from, String groupName ) {
        this.from = from;
        this.groupName = groupName;
    }

    @Override
    public int getMessageType() {
        return GroupJoinResponseMessage;
    }
}
