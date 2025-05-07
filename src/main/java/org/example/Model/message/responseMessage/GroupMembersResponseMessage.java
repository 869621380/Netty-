package org.example.Model.message.responseMessage;
import lombok.Data;
import lombok.ToString;

import java.util.Set;

@Data
@ToString(callSuper = true)
public class GroupMembersResponseMessage extends RequestResponseMessage{
    private Set<Integer> members;

    public GroupMembersResponseMessage(Set<Integer> members) {
        this.members = members;
    }

    @Override
    public int getMessageType() {
        return GroupMembersResponseMessage;
    }
}
