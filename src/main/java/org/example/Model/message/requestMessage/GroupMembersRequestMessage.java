package org.example.Model.message.requestMessage;

import lombok.Data;
import lombok.ToString;


@ToString(callSuper = true)
public class GroupMembersRequestMessage extends GroupChatRequestMessage{
    //private String groupName;

    public GroupMembersRequestMessage(String groupName) {
        super(groupName);
        //this.groupName = groupName;
    }


    @Override
    public int getMessageType() {
        return GroupMembersMessage;
    }
}
