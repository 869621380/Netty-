package org.example.Dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.example.entity.friendships;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipsMapper extends BaseMapper<friendships> {
    // 根据 user1_id 查询对应的 user2_id 列表
    @Select("SELECT user2_id FROM friendships WHERE user1_id = #{user1_id}")
    List<Integer> selectFriendIdsByUserId1(int user1_id);

    // 根据 user2_id 查询对应的 user1_id 列表
    @Select("SELECT user1_id FROM friendships WHERE user2_id = #{user2_id}")
    List<Integer> selectFriendIdsByUserId2(int user2_id);
}
