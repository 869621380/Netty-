// 2. 搜索好友响应消息
package org.example.Model.message.responseMessage;

import lombok.Data;
import org.example.Model.Domain.UserInfo;
import org.example.Model.message.Message;

@Data
public class SearchFriendResponseMessage extends Message {
    private boolean success;
    private String reason;
    private UserInfo userInfo; // 包含用户ID、用户名、头像等信息
    private boolean alreadyFriend; // 是否已经是好友
    
    public SearchFriendResponseMessage() {
    }
    
    public SearchFriendResponseMessage(boolean success, String reason) {
        this.success = success;
        this.reason = reason;
    }
    
    public SearchFriendResponseMessage(boolean success, UserInfo userInfo, boolean alreadyFriend) {
        this.success = success;
        this.userInfo = userInfo;
        this.alreadyFriend = alreadyFriend;
    }
    
    @Override
    public int getMessageType() {
        return SearchFriendResponseMessage;
    }
}