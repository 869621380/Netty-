package org.example.Model.message.requestMessage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.Model.message.Message;

@EqualsAndHashCode(callSuper = true)
@Data
public class FriendRequestMessage extends Message {
    private String userId; //请求方id
    private String friendId; //被请求方id

    @Override
    public int getMessageType(){
        return FriendRequest;
    }
}