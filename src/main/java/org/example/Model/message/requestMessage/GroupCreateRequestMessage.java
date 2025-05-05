package org.example.Model.message.requestMessage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.Model.message.Message;

import java.util.List;
@Data
@EqualsAndHashCode(callSuper = true)
public class GroupCreateRequestMessage extends Message {
    private String groupName;
    private String groupAvatar;
    private String ownerId;
    private List<String> initialMembers;

    @Override
    public int getMessageType() {
        return GroupCreateRequest;  
    }

}
