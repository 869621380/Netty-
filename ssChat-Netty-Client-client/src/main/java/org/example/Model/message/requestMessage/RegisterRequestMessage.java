package org.example.Model.message.requestMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@AllArgsConstructor
public class RegisterRequestMessage extends LoginAndRegisterRequestMessage {
    String email;
    String code;
    String password;
    @Override
    public int getMessageType() {
        return RegisterRequestEvent;
    }
}
