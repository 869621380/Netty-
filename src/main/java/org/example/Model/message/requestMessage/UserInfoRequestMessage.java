package org.example.Model.message.requestMessage;

import lombok.Data;
import org.example.Model.message.Message;

/**
 * 用户信息请求消息
 * 用于请求指定用户的详细信息
 */
@Data
public class UserInfoRequestMessage extends Message {
    private Integer senderId;    // 请求者ID
    private Integer receiverId;  // 要获取信息的用户ID

    public UserInfoRequestMessage() {
        // 无参构造函数用于反序列化
    }

    public UserInfoRequestMessage(Integer senderId, Integer receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    @Override
    public int getMessageType() {
        return UserInfoRequestMessage;
    }
}
