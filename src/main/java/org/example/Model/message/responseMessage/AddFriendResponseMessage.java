// 4. 添加好友响应消息
package org.example.Model.message.responseMessage;

import lombok.Data;
import org.example.Model.Domain.ChatItem;
import org.example.Model.message.Message;

@Data
public class AddFriendResponseMessage extends Message {
    private boolean success;
    private String reason;
    private Object newFriend; // 新添加的好友信息
    
    public AddFriendResponseMessage() {
    }
    
    public AddFriendResponseMessage(boolean success, String reason) {
        this.success = success;
        this.reason = reason;
    }
    
    public AddFriendResponseMessage(boolean success, ChatItem newFriend) {
        this.success = success;
        this.newFriend = newFriend;
    }
    
    @Override
    public int getMessageType() {
        return AddFriendResponseMessage;
    }
}