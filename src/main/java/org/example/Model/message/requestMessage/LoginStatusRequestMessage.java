package org.example.Model.message.requestMessage;

import org.example.Model.message.Message;

public class LoginStatusRequestMessage extends Message {
    private Integer receiverId;

    public LoginStatusRequestMessage(Integer receiverId) {
        super();
        this.receiverId = receiverId;
    }

    @Override
    public int getMessageType() {
        return LoginStatusRequestMessage;
    }
}
