package org.example.Model.message;

import lombok.Data;

@Data
public abstract class GroupChatChatMessage extends ChatMessage {
    //群组ID
    protected Integer groupID;
}
