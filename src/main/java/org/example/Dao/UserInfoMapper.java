package org.example.Dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.Model.Domain.UserInfo;

import java.util.List;


public interface UserInfoMapper {

    @Select("SELECT nickname, avatar_path " +
            "FROM user_information " +
            "WHERE id IN (#{senderId}, #{receiverId}) " +
            "ORDER BY " +
            "    CASE id " +
            "        WHEN #{senderId} THEN 1 " +
            "        WHEN #{receiverId} THEN 2 " +
            "    END;")
    List<UserInfo> getUserInfoByIds(@Param("senderId") Integer senderId, @Param("receiverId") Integer receiverId);


}
