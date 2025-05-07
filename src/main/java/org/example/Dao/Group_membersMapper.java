package org.example.Dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.example.Model.Domain.ChatItem;
import org.example.entity.group_members;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Group_membersMapper extends BaseMapper<group_members> {
    @Select("SELECT group_name ," +
            "1 AS receiverType ," +
            "-1 AS receiverId ," +
            "'img.png' AS avatarPath ," +
            "0 AS unreadCount ," +
            "'' AS preview ," +
            "'' AS previewTime " +
            " FROM group_members " +
            "WHERE user_id = #{userId}")
    @Results({
            @Result(property = "receiverType",column = "receiverType"),
            @Result(property = "receiverId",column = "receiverId"),
            @Result(property = "receiverName", column = "group_name"),
            @Result(property = "avatarPath",column = "avatarPath"),
            @Result(property = "unreadCount",column = "unreadCount" ),
            @Result(property = "preview",column = "preview"),
            @Result(property = "previewTime",column = "previewTime")
    })
    List<ChatItem> selectGroupNameByUserId(@Param("userId") Integer userId);

    @Insert("INSERT INTO group_members (group_name, user_id) VALUES (#{groupName}, #{userId})")
    public int joinMember(@Param("groupName") String groupName, @Param("userId") Integer userId);

}
