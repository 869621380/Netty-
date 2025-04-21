package org.example.Model.message.responseMessage;


import lombok.Getter;

public class LoginRequestResponseMessage extends RequestResponseMessage {
    @Override
    public int getMessageType() {
        return LoginResponseEvent;
    }
    @Getter
    String token;
    public LoginRequestResponseMessage(boolean success, String userId, String token) {
        super(success, userId);
        this.token = token;
    }
}
