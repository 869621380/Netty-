package org.example.Service;

import org.example.Dao.ChatListMapper;
import org.example.Model.Domain.ChatItem;
import org.example.Util.MyBatisUtil;

import java.util.List;

public class ChatListService {
    private final ChatListMapper chatListMapper;
    public ChatListService() {
        this.chatListMapper= MyBatisUtil.chatListMapper;
    }


    public List<ChatItem> getChatItems(Integer userId) {
        return chatListMapper.getChatItems(userId);
    }
}
