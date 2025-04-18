package org.example.Service;

import org.example.Dao.UserInfoMapper;
import org.example.Model.Domain.UserInfo;
import org.example.Util.MyBatisUtil;

import java.util.List;

public class UserInfoService {
    UserInfoMapper userInfoMapper;
    public UserInfoService(){
        userInfoMapper= MyBatisUtil.userInfoMapper;
    }

    /**
     *
     * @param senderId
     * @param receiverId
     * @return 发送方和接收方的ID和头像
     */
    public List<UserInfo> getUserAvatar(int senderId, int receiverId) {
        return userInfoMapper.getUserInfoByIds(senderId, receiverId);
    }
}
