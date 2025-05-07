package org.example.Dao;

import org.apache.ibatis.annotations.Select;
import org.example.Model.Domain.ChatItem;

import java.util.List;

public interface ChatListMapper {
    @Select("select receiver_type,receiver_id,nickname as receiver_name,avatar_path,unreadCount,preview,preview_time\n" +
            "from chat_list c join user_information u on(c.receiver_id=u.id)\n" +
            "where sender_id=#{userId};")
    List<ChatItem> getChatItems(Integer userId);
}
