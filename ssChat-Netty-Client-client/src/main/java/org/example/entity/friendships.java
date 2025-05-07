package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("friendships")
public class friendships {
    @TableId(value = "friendship_id")
    private int friendship_id;
    @TableField("user1_id")
    private int user1_id;
    @TableField("user2_id")
    private int user2_id;
}
