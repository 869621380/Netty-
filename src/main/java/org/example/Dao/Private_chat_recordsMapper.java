package org.example.Dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.example.Model.Domain.SingleChatMessage;
import org.example.entity.private_chat_records;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface Private_chat_recordsMapper extends BaseMapper<private_chat_records> {


    @Select("SELECT " +
            "record_id, " +
            "sender_id, " +
            "receiver_id, " +
            "message, " +
            "'已发送' AS send_status, "+
            "'text' AS type,"+
            "DATE_FORMAT(send_time, '%Y-%m-%d %H:%i:%s') AS send_time " +
            "FROM private_chat_records " +
            "WHERE (((sender_id = #{sender_id} AND receiver_id = #{receiver_id}) "+
            "OR (sender_id=#{receiver_id} AND receiver_id=#{sender_id})) AND master_id=#{sender_id})" +
            "ORDER BY send_time ASC")
    @Results({
            @Result(property = "sendTime", column = "send_time"),
            @Result(property = "senderID", column = "sender_id"),
            @Result(property = "receiverID",column = "receiver_id"),
            @Result(property = "type",column = "type"),
            @Result(property = "content",column="message"),

    })
    List<SingleChatMessage> selectMessagesBySenderAndReceiver(@Param("sender_id")int senderID, @Param("receiver_id") int receiverID);



    @Insert("INSERT INTO private_chat_records (master_id,sender_id, receiver_id, message) " +
            "VALUES (#{master_id},#{sender_id}, #{receiver_id},#{message} ); ")
    int insertSingleMessage(@Param("master_id")int master_id,@Param("sender_id")int sender_id, @Param("receiver_id") int receiver_id, @Param("message") Object message);
}
