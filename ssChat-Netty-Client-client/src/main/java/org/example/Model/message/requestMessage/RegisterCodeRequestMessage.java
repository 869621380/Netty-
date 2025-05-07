package org.example.Model.message.requestMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class RegisterCodeRequestMessage extends LoginAndRegisterRequestMessage{
    @Getter
    private String email;
    @Override
    public int getMessageType() {
        return RegisterCodeRequestMessage;
    }


}
