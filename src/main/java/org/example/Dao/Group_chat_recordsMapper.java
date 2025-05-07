package org.example.Dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.example.Model.Domain.GroupChatMessage;
import org.example.entity.group_chat_records;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Group_chat_recordsMapper extends BaseMapper<group_chat_records> {

    @Select("SELECT " +
            "    send_time,  " +
            "    sender_id,  " +
            "    group_name,  " +
            "    message,  " +
            "    userName, " +
            "    '已发送' AS send_status,  " +
            "    'text' AS type  " +
            "FROM  " +
            "    group_chat_records g  " +
            "JOIN  " +
            "    user u ON g.sender_id = u.userID " +
            "WHERE  " +
            "    g.group_name = #{groupName} " +
            "ORDER BY send_time ASC;")
    @Results({
            @Result(property = "sendTime", column = "send_time"),
            @Result(property = "senderID",column = "sender_id"),
            @Result(property = "groupName", column = "group_name"),
            @Result(property = "content",column = "message"),
            @Result(property = "sendStatus",column = "send_status"),
            @Result(property = "type",column = "type"),
            @Result(property = "senderName",column = "username")
    })

    List<GroupChatMessage> selectMessagesBySenderIdAndGroupName(@Param("groupName") String groupName);



    @Insert("INSERT INTO group_chat_records (sender_id,group_name,message) " +
            "VALUES (#{sender_id}, #{group_name},#{message} )")
    int insertGroupMessage(@Param("sender_id") Integer senderID,@Param("group_name") String groupName,@Param("message") Object content);
}
