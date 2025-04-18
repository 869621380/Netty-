package org.example.Service;

import org.example.Dao.ChatMessageMapper;
import org.example.Model.Domain.SingleChatMessage;
import org.example.Model.message.requestMessage.SingleChatRequestMessage;
import org.example.Model.message.requestMessage.SingleChatTextRequestMessage;
import org.example.Util.MyBatisUtil;

import java.util.List;

public class ChatMessageService {
    ChatMessageMapper chatMessageMapper;
    public ChatMessageService() {
        chatMessageMapper= MyBatisUtil.chatMessageMapper;
    }

    /**
     *
     * @param senderId
     * @param receiverId
     * @return 双方聊天消息
     */
    public List<SingleChatMessage> getSingleChatTextMessage(Integer senderId, Integer receiverId){
        return chatMessageMapper.getSingleChatMessage(senderId,receiverId);
    }

}
