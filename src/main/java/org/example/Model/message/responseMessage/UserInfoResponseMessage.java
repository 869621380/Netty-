package org.example.Model.message.responseMessage;

import lombok.Data;
import org.example.Model.Domain.UserInfo;
import org.example.Model.message.Message;

import java.util.List;

/**
 * 用户信息响应消息
 * 包含查询的用户信息结果
 */
@Data
public class UserInfoResponseMessage extends Message {
    private boolean success;         // 查询是否成功
    private String reason;           // 失败原因
    private List<UserInfo> userInfos; // 用户信息列表

    public UserInfoResponseMessage() {
        // 无参构造函数用于反序列化
    }

    public UserInfoResponseMessage(boolean success, List<UserInfo> userInfos) {
        this.success = success;
        this.userInfos = userInfos;
    }

    public UserInfoResponseMessage(boolean success, String reason) {
        this.success = success;
        this.reason = reason;
    }

    @Override
    public int getMessageType() {
        return UserInfoResponseMessage;
    }
}