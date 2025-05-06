package org.example.Dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.Model.Domain.SingleChatMessage;
import org.example.Model.message.requestMessage.SingleChatTextRequestMessage;

import java.util.List;

public interface ChatMessageMapper {
    @Select("SELECT sender_id,receiver_id,type,message,send_time,send_status " +
            "FROM private_chat_records m " +
            "where ((m.sender_id=#{senderId} AND m.receiver_id=#{receiverId})" +
            "OR (m.sender_id=#{receiverId} AND m.receiver_id=#{senderId}))" +
            "ORDER BY send_time DESC"
    )
    public List<SingleChatMessage> getSingleChatMessage(@Param("senderId") Integer senderId, @Param("receiverId") Integer receiverId);


}
