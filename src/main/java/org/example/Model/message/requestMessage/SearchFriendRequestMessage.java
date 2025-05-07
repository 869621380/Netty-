// 1. 搜索好友请求消息
package org.example.Model.message.requestMessage;

import lombok.Data;
import org.example.Model.message.Message;

@Data
public class SearchFriendRequestMessage extends Message {
    private Integer userId; // 当前用户ID
    private Integer friendId; // 要搜索的用户ID
    
    public SearchFriendRequestMessage(Integer userId, Integer friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }
    
    @Override
    public int getMessageType() {
        return SearchFriendRequestMessage;
    }
}