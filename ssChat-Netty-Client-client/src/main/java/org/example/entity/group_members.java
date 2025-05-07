package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("group_members")
public class group_members {
    @TableId(value = "membership_id")
    private int membership_id;
    @TableField("group_name")
    private String group_name;
    @TableField("user_id")
    private int user_id;
}
