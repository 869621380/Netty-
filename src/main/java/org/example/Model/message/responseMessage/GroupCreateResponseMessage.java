package org.example.Model.message.responseMessage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.Model.Domain.GroupInfo;
import org.example.Model.message.Message;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupCreateResponseMessage extends Message {
    private boolean success;
    private String reason;
    private GroupInfo groupInfo;

    @Override
    public int getMessageType() {
        return GroupCreateResponse;  
    }
}
