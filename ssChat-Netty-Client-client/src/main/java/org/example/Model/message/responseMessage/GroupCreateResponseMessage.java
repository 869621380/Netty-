package org.example.Model.message.responseMessage;
import lombok.Data;
import lombok.ToString;

import java.util.Set;

@Data
@ToString(callSuper = true)
public class GroupCreateResponseMessage extends RequestResponseMessage{

    Integer createrId;
    Set<Integer> groupMember;
    public GroupCreateResponseMessage(boolean success, String reason,Integer createrId,Set<Integer>groupMember) {
        super(success, reason);
        this.createrId=createrId;
        this.groupMember=groupMember;
    }
    @Override
    public int getMessageType() {
        return GroupCreateResponseMessage;
    }
}
