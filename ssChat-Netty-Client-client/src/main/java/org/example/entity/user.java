package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class user {
    @TableId("UserID")
    private int UserID;
    @TableField("username")
    private String username;
    @TableField("password")
    private String password;
}
