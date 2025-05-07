package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@TableName("private_chat_records")
public class private_chat_records {
    @TableId(value = "record_id")
    private int record_id;
    @TableField("sender_id")
    private int sender_id;
    @TableField("receiver_id")
    private int receiver_id;
    @TableField("message")
    private String message;
    @TableField("send_time")
    private LocalDateTime send_time;
}
