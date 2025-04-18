package org.example.Model.message.responseMessage;


public class LoginRequestResponseMessage extends RequestResponseMessage {
    @Override
    public int getMessageType() {
        return LoginResponseEvent;
    }

    public LoginRequestResponseMessage(boolean success, String userId) {
        super(success, userId);
    }
}
