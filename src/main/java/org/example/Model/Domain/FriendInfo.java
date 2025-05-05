package org.example.Model.Domain;
import lombok.Data;

@Data
public class FriendInfo {
    private String userid;
    private String friendId;
    private String friendNickName;
    private String friendAvatar;
    private boolean online;     //在线状态
    private String createTime;
}
