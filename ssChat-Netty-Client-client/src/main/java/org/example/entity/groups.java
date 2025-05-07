package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("groups")
public class groups {
    @TableId(value = "group_name")
    private String group_name;
    @TableField("creator_id")
    private int creator_id;
}
