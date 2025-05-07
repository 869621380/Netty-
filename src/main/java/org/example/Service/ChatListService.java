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
import java.util.Set;

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
    private final Group_membersMapper group_membersMapper;
    private final GroupsMapper groupsMapper;
    public ChatListService() {
        this.friendshipsMapper = MyBatisUtil.friendshipsMapper;
        this.userMapper = MyBatisUtil.userMapper;
        this.group_membersMapper=MyBatisUtil.group_membersMapper;
        this.groupsMapper = MyBatisUtil.groupsMapper;
    }

    public List<ChatItem> getChatItems(Integer userId) {
        //return chatListMapper.getChatItems(userId);
        //获取当前用户所有好友ID
        List<Integer> friendsID=friendshipsMapper.selectFriendIdsByUserId1(userId);
        List<ChatItem> groupsItem=group_membersMapper.selectGroupNameByUserId(userId);
        if (!friendsID.isEmpty() ){
            List<ChatItem> friendsItem = userMapper.selectChatItemsByIds(friendsID);
            friendsItem.addAll(groupsItem);
            return friendsItem;
        }
        return null;
    }




    public void sendInitMessage(Integer userId, ChannelHandlerContext ctx) {
        InitOKMessage initOKMessage = new InitOKMessage();
        initOKMessage.setUserId(userId);
        ctx.writeAndFlush(initOKMessage);
        log.debug("发送了离线数据请求");
    }

    public int createGroup(Integer creator, String name, Set<Integer> members) {

        int t = groupsMapper.createGroup(name, creator);
        if (t != 0) {
            for (Integer i : members) {
                joinMember(name, i);
            }
            return t;
        } else {
            return 0;
        }
    }

    public int joinMember(String name, Integer member) {
        return group_membersMapper.joinMember(name, member);
    }
}

