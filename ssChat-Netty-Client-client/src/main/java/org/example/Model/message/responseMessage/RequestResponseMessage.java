package org.example.Model.message.responseMessage;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.Model.message.Message;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class RequestResponseMessage extends Message {
    protected boolean success;
    protected String reason;
}
