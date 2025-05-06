package org.example.Model.message;

import lombok.Data;

@Data
public class InitOKMessage extends Message{
    private Integer userId;
    @Override
    public int getMessageType() {
        return InitOKMessage;
    }
}
