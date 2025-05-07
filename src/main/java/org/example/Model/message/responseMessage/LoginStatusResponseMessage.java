package org.example.Model.message.responseMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.Model.message.Message;

@Data
@AllArgsConstructor
public class LoginStatusResponseMessage extends Message {
    String loginStatus;
    Integer receiverId;
    @Override
    public int getMessageType() {
        return LoginStatusResponseMessage;
    }
}
