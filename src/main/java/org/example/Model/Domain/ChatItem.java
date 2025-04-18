package org.example.Model.Domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Format;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatItem {

        //0表示单聊 1表示群聊
        private Integer receiverType;
        //接收人/群聊ID
        private Integer receiverId;
        //接收人名称
        private String receiverName;
        //头像路径
        private String avatarPath;
        //未读数量
        private  int unreadCount;
        //预览消息
        private  String preview;
        //预览消息时间
        private String previewTime;

}