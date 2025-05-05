package org.example.Dao;

import  org.apache.ibatis.annotations.*;
import org.example.Model.Domain.GroupInfo;

import java.util.List;

public interface GroupMapper {
//    @Insert("INSERT INTO group_info (group_id,group_name,group_avatar,owner_id)" +
//    "VALUES (#{groupId},#{groupName},#{groupAvatar},#{ownerId})")
//    int saveGroup(GroupInfo group);
//
//    @Select("SELECT g.* FROM group_info g JOIN group_member m ON g.group_id = group_id" +
//    "WHERE m.member_id = #{userId}")
//    List<GroupInfo>getUserGroups(String userId);
//
//    @Select("SELECT * FROM group_info WHERE group_id = #{groupId}")
//    GroupInfo getGroupById(String groupId);

    @Insert("INSERT INTO group_info (group_id, group_name, group_avatar, owner_id)" +
            "VALUES (#{groupId}, #{groupName}, #{groupAvatar}, #{ownerId})")
    int saveGroup(GroupInfo group);

    @Select("SELECT g.* FROM group_info g JOIN group_member m ON g.group_id = m.group_id " +
            "WHERE m.member_id = #{userId}")
    List<GroupInfo> getUserGroups(String userId);

    @Select("SELECT * FROM group_info WHERE group_id = #{groupId}")
    GroupInfo getGroupById(String groupId);

    // 添加群组成员方法
    @Insert("INSERT INTO group_member (group_id, member_id, join_time) " +
            "VALUES (#{groupId}, #{memberId}, NOW())")
    int addGroupMember(@Param("groupId") String groupId, @Param("memberId") String memberId);
}
