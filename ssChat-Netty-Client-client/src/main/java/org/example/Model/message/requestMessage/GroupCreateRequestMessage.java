package org.example.Model.message.requestMessage;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Data
@ToString(callSuper = true)
public class GroupCreateRequestMessage extends GroupChatRequestMessage{
    //private String groupName;
    private Set<Integer> members;
    private Integer creator;

    public GroupCreateRequestMessage(Integer creator,String groupName, Set<Integer> members) {
        super(groupName);
        this.creator = creator;
        this.groupName = groupName;
        this.members = members;
    }

    @Override
    public int getMessageType() {
        return GroupCreateMessage;
    }
}