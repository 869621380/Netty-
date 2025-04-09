package org.example.Model.message;


import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class LoginRequestMessage extends LoginMessage {

    private final String username;
    private final String password;



    public LoginRequestMessage(String username, String password) {
        this.username = username;
        this.password = password;

    }

    @Override
    public int getMessageType() {
        return LoginRequestEvent;
    }
}
