package org.example.Model.message;

public class RegisterResponseMessage extends LoginMessage {
    @Override
    public int getMessageType() {
        return RegisterResponseEvent;
    }
}
