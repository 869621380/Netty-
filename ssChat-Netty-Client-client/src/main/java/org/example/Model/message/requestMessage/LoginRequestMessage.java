package org.example.Model.message.requestMessage;


import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class LoginRequestMessage extends LoginAndRegisterRequestMessage {

    private final Integer userId;
    private final String password;



    public LoginRequestMessage(Integer userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    @Override
    public int getMessageType() {
        return LoginRequestEvent;
    }
}
