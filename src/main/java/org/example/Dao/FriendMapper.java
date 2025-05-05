//package org.example.Dao;
//
//import org.apache.ibatis.annotations.Param;
//import org.example.Model.Domain.FriendInfo;
//import java.util.List;
//
//public interface FriendMapper {
//    // 添加好友
//    int addFriend(FriendInfo friendInfo);
//
//    // 更新好友信息
//    int updateFriend(FriendInfo friendInfo);
//
//    // 查找特定好友
//    FriendInfo findFriendById(@Param("userId") Integer userId, @Param("friendId") Integer friendId);
//
//    // 获取好友列表
//    List<FriendInfo> getAllFriends(@Param("userId") String userId);
//}

package org.example.Dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.Model.Domain.FriendInfo;
import java.util.List;

public interface FriendMapper {
    @Insert("INSERT INTO friend_list (user_id, friend_id, friend_nickname, friend_avatar) " +
            "VALUES (#{userid}, #{friendId}, #{friendNickName}, #{friendAvatar})")
    int addFriend(FriendInfo friendInfo);

    @Update("UPDATE friend_list SET friend_nickname = #{friendNickName}, " +
            "friend_avatar = #{friendAvatar} WHERE user_id = #{userid} AND friend_id = #{friendId}")
    int updateFriend(FriendInfo friendInfo);

    @Select("SELECT * FROM friend_list WHERE user_id = #{userId} AND friend_id = #{friendId}")
    FriendInfo findFriendById(@Param("userId") Integer userId, @Param("friendId") Integer friendId);

    @Select("SELECT * FROM friend_list WHERE user_id = #{userId}")
    List<FriendInfo> getAllFriends(@Param("userId") String userId);

    @Delete("DELETE FROM friend_list WHERE user_id = #{userId} AND friend_id = #{friendId}")
    int deleteFriend(@Param("userId") String userId, @Param("friendId") String friendId);
}