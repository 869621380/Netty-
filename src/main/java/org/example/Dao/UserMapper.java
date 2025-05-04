package org.example.Dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.example.Model.Domain.ChatItem;
import org.example.Model.Domain.UserInfo;
import org.example.entity.user;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper extends BaseMapper<user> {
    // 根据 UserID 列表查询用户名列表
    @Select("<script>" +
            "SELECT username,UserID FROM user WHERE UserID IN " +
            "<foreach collection='userIds' item='userId' open='(' close=')' separator=','>" +
            "#{userId}" +
            "</foreach>" +
            "</script>")
    @Results({
            //@Result(property = "receiverType"),
            @Result(property = "receiverId", column = "UserID"),
            @Result(property = "receiverName", column = "username"),
            //@Result(property = "avatarPath"),
            //@Result(property = "unreadCount"),
            //@Result(property = "preview"),
            //@Result(property = "previewTime")
    })
    List<ChatItem> selectChatItemsByIds(@Param("userIds") List<Integer> userIds);


    @Select("SELECT username,UserID " +
            "FROM user " +
            "WHERE UserID IN (#{senderId}, #{receiverId}) " +
            "ORDER BY " +
            "    CASE UserID " +
            "        WHEN #{senderId} THEN 1 " +
            "        WHEN #{receiverId} THEN 2 " +
            "    END;")
    @Results({
            @Result(property = "nickname", column = "username"),
            @Result(property = "id",column = "UserID")
    })
    List<UserInfo> getUserInfoByIds(@Param("senderId") Integer senderId, @Param("receiverId") Integer receiverId);
}
