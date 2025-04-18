package org.example.Model.message.responseMessage;

import org.example.Model.message.Message;

public class RegisterRequestResponseMessage extends RequestResponseMessage {
    @Override
    public int getMessageType() {
        return Message.RegisterResponseEvent;
    }

    public RegisterRequestResponseMessage(boolean success, String reason) {
        super(success, reason);
    }
}
