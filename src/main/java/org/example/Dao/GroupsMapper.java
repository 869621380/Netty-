package org.example.Dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.example.entity.groups;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupsMapper extends BaseMapper<groups> {
    @Insert("INSERT INTO `groups` (group_name, creator_id) VALUES (#{groupName}, #{creatorId})")
    public int createGroup(@Param("groupName") String groupName, @Param("creatorId") Integer creatorId);
}
