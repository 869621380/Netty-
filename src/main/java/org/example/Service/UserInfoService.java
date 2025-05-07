package org.example.Service;

import org.example.Dao.UserInfoMapper;
import org.example.Dao.UserMapper;
import org.example.Model.Domain.UserInfo;
import org.example.Model.message.requestMessage.UserInfoRequestMessage;
import org.example.Util.MyBatisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public class UserInfoService {
    private static final Logger log = LoggerFactory.getLogger(UserInfoService.class);
    UserInfoMapper userInfoMapper;
    UserMapper userMapper;
    public UserInfoService(){
        userInfoMapper= MyBatisUtil.userInfoMapper;
        userMapper= MyBatisUtil.userMapper;
    }

    /**
     *
     * @param senderId
     * @param receiverId
     * @return 发送方和接收方的ID和头像
     */
    public List<UserInfo> getUserAvatar(int senderId, int receiverId) {

        //return userInfoMapper.getUserInfoByIds(senderId, receiverId);
        return userMapper.getUserInfoByIds(senderId,receiverId); //返回的是发送方和接收方的UserInfo
    }

    public String getNameById(Integer senderID) {
        return userMapper.getNameById(senderID);
    }

    /**
     * 向服务器请求用户信息
     * @param senderId 发送者ID
     * @param receiverId 接收者ID
     * @param ctx 通道上下文
     */
    public void requestUserInfo(Integer senderId, Integer receiverId, ChannelHandlerContext ctx) {
        try {
            log.info("请求用户信息: senderId={}, receiverId={}", senderId, receiverId);

            // 创建用户信息请求消息
            UserInfoRequestMessage message = new UserInfoRequestMessage(senderId, receiverId);

            // 发送请求
            ctx.writeAndFlush(message);

        } catch (Exception e) {
            log.error("请求用户信息失败", e);
        }
    }
}
