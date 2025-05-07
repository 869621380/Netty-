package org.example.Model.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IdentityVerifyMessage extends Message{
    private String token;

    @Override
    public int getMessageType() {
        return IdentityVerifyMessage;
    }
}
