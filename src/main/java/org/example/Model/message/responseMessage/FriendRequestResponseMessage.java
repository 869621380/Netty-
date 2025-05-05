//package org.example.Model.message.responseMessage;
//
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import org.example.Model.Domain.FriendInfo;
//import org.example.Model.message.Message;
//
//@Data
//@EqualsAndHashCode(callSuper = true)
//public class FriendRequestResponseMessage extends Message {
//    private boolean success;
//    private String reason;
//    private FriendInfo friendInfo;
//
//    @Override
//    public int getMessageType() {
//        return FriendRequestResponse;
//    }
//}

package org.example.Model.message.responseMessage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.Model.message.Message;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class FriendRequestResponseMessage extends Message {
    private boolean success;
    private String reason;
    private Map<String, Object> friendInfo;

    @Override
    public int getMessageType() {
        return FriendRequestResponse;  
    }
}