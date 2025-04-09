package org.example.Model.message;

public class RegisterRequestMessage extends LoginMessage{
    @Override
    public int getMessageType() {
        return RegisterRequestEvent;
    }
}
