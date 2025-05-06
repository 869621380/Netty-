package org.example.Service;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.example.Dao.*;
import org.example.Model.Domain.ChatItem;
import org.example.Model.message.InitOKMessage;
import org.example.Util.MyBatisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service("chatListService")
public class ChatListService {
//    private final ChatListMapper chatListMapper;
//    public ChatListService() {
//        this.chatListMapper= MyBatisUtil.chatListMapper;
//    }
//
    private final FriendshipsMapper friendshipsMapper;

    private final UserMapper userMapper;

    public ChatListService() {
        this.friendshipsMapper = MyBatisUtil.friendshipsMapper;
        this.userMapper = MyBatisUtil.userMapper;
    }

    public List<ChatItem> getChatItems(Integer userId) {
        //return chatListMapper.getChatItems(userId);
        //获取当前用户所有好友ID
        List<Integer> friendsID=friendshipsMapper.selectFriendIdsByUserId1(userId);
        if (!friendsID.isEmpty()){
            List<ChatItem> friendsItem = userMapper.selectChatItemsByIds(friendsID);
            return friendsItem;
        }
        return null;
    }


    public ChatItem createGroup(Integer creatorId, String groupName, List<Integer> memberIds) {
        // 实现创建群聊的逻辑
        // 返回新创建的群聊项
        ChatItem group = new ChatItem();
        group.setReceiverId(-1); // 假设这是群ID
        group.setReceiverName(groupName);
        group.setPreview("[新群聊]");
        group.setPreviewTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        group.setUnreadCount(0);
        group.setAvatarPath("img.png"); // 需要定义这个常量

        return group;
    }

    public void sendInitMessage(Integer userId, ChannelHandlerContext ctx) {
        InitOKMessage initOKMessage = new InitOKMessage();
        initOKMessage.setUserId(userId);
        ctx.writeAndFlush(initOKMessage);
        log.debug("发送了离线数据请求");
    }
}
