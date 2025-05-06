package org.example.Service;

import org.example.Dao.*;
import org.example.Model.Domain.ChatItem;
import org.example.Util.MyBatisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    public ChatListService() {
        this.friendshipsMapper = MyBatisUtil.friendshipsMapper;
        this.userMapper = MyBatisUtil.userMapper;
        this.group_membersMapper=MyBatisUtil.group_membersMapper;
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



}
