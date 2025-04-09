package org.example.Model.message;

public class LoginResponseMessage extends LoginMessage {
    @Override
    public int getMessageType() {
        return LoginResponseEvent;
    }

}
