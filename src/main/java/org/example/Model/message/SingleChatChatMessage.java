package org.example.Model.message;

import lombok.Data;

@Data
public abstract class SingleChatChatMessage extends ChatMessage {
    protected Integer receiverID;
}
