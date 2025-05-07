// 3. 添加好友请求消息
package org.example.Model.message.requestMessage;

import lombok.Data;
import org.example.Model.message.Message;

@Data
public class AddFriendRequestMessage extends Message {
    private Integer userId; // 当前用户ID
    private Integer friendId; // 要添加的好友ID
    
    public AddFriendRequestMessage(Integer userId, Integer friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }
    
    @Override
    public int getMessageType() {
        return AddFriendRequestMessage;
    }
}