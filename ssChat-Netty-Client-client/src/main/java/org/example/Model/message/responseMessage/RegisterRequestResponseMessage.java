package org.example.Model.message.responseMessage;

import lombok.Data;
import org.example.Model.message.Message;

@Data
public class RegisterRequestResponseMessage extends RequestResponseMessage {
    @Override
    public int getMessageType() {
        return Message.RegisterResponseEvent;
    }


    public RegisterRequestResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

}
