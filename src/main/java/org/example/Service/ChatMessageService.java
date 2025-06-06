package org.example.Service;

import io.netty.channel.ChannelHandlerContext;
import org.example.Cache.MessageCache;
import org.example.Dao.ChatMessageMapper;
import org.example.Dao.Group_chat_recordsMapper;
import org.example.Dao.Private_chat_recordsMapper;
import org.example.Model.Domain.GroupChatMessage;
import org.example.Model.Domain.Message;
import org.example.Model.Domain.SingleChatMessage;
import org.example.Model.message.requestMessage.*;
import org.example.Util.MyBatisUtil;
import org.example.Util.ThreadPoolManager;
import org.example.entity.private_chat_records;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatMessageService {
    private static final Logger log = LoggerFactory.getLogger(ChatMessageService.class);

    ChatMessageMapper chatMessageMapper;
    Private_chat_recordsMapper private_chat_recordsMapper;
    public Group_chat_recordsMapper groupChatRecordsMapper;
    ;
    public ChatMessageService() {
        chatMessageMapper= MyBatisUtil.chatMessageMapper;
        private_chat_recordsMapper=MyBatisUtil.private_chat_recordsMapper;
        groupChatRecordsMapper=MyBatisUtil.groupChatRecordsMapper;

    }

    /**
     *
     * @param senderId
     * @param receiverId
     * @return 双方聊天消息
     */
    public List<SingleChatMessage> getSingleChatTextMessage(Integer senderId, Integer receiverId){

        //return chatMessageMapper.getSingleChatMessage(senderId,receiverId);

        return private_chat_recordsMapper.selectMessagesBySenderAndReceiver(senderId,receiverId);

    }

    public List<GroupChatMessage> getGroupChatTextMessage(String receiverName) {
        return groupChatRecordsMapper.selectMessagesBySenderIdAndGroupName(receiverName);
    }

    public void sendMessage(SingleChatMessage content,ChannelHandlerContext ctx){
        System.out.println("消息类型："+content.getType());
        SingleChatRequestMessage singleChatRequestMessage = null;
        if(content.getType().equals("text")){
            singleChatRequestMessage=new SingleChatTextRequestMessage(content.getSendTime(),content.getSenderID(),content.getReceiverID(),(String) content.getContent());
            MessageCache.getChatListController().updatePreview(content.getReceiverID(), (String) content.getContent());
            int result=private_chat_recordsMapper.insertSingleMessage(content.getSenderID(),content.getSenderID(),content.getReceiverID(),content.getContent());
            System.out.println("success:"+result);
        }
        else if(content.getType().equals("image")){

            singleChatRequestMessage=new SingleChatImageRequestMessage(content.getSendTime(),content.getSenderID(),content.getReceiverID(),(byte[])content.getContent());
            MessageCache.getChatListController().updatePreview(content.getReceiverID(),"[图片]");

        }

        if(ctx!=null&&singleChatRequestMessage!=null){
            //      MessageCache.addMessageCache(singleChatRequestMessage.getSequenceId(),content);
            //这里应该响应后再SENT
            content.changeSendStatus(Message.SENT);
            ctx.writeAndFlush(singleChatRequestMessage);
            log.debug("HERE SEND MESSAGE"+singleChatRequestMessage.getMessageType());
        }else if(singleChatRequestMessage!=null){
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                Integer count=0;
                @Override
                public void run() {
                    if(count==5&&ctx==null){
                        content.changeSendStatus(Message.FAILED);
                        timer.cancel();
                    }
                    else if(ctx!=null){
                        content.changeSendStatus(Message.SENT);
                        ctx.writeAndFlush(content);
                        timer.cancel();
                    }
                    ++count;
                }
            };
            timer.scheduleAtFixedRate(task, 0, 1000);
        }

    }

    public void sendGroupMessage(GroupChatMessage content, ChannelHandlerContext ctx) {
        System.out.println("消息类型："+content.getType());
        GroupChatRequestMessage groupChatRequestMessage = null;
        //System.out.println("groupName:"+content.getGroupName());
        if(content.getType().equals("text")){
            groupChatRequestMessage=new GroupChatTextRequestMessage(content.getSendTime(),content.getSenderID(),content.getGroupName(),(String) content.getContent());
            //补全preview
            MessageCache.getChatListController().updateGroupPreview(content.getGroupName(), (String) content.getContent());
            int result=groupChatRecordsMapper.insertGroupMessage(content.getSenderID(),content.getGroupName(),content.getContent());
            System.out.println("success:"+result);
        }
        else if(content.getType().equals("image")){

            groupChatRequestMessage=new GroupChatImageRequestMessage(content.getSendTime(),content.getSenderID(),content.getGroupName(),(byte[])content.getContent());
            //补全preview
            MessageCache.getChatListController().updateGroupPreview(content.getGroupName(),"[图片]");
        }
        System.out.println("ctx=="+ctx);
        if(ctx!=null&&groupChatRequestMessage!=null){
            //      MessageCache.addMessageCache(singleChatRequestMessage.getSequenceId(),content);
            //这里应该响应后再SENT
            content.changeSendStatus(Message.SENT);
            ctx.writeAndFlush(groupChatRequestMessage);
            log.debug("HERE SEND MESSAGE"+groupChatRequestMessage.getMessageType());
        }else if(groupChatRequestMessage!=null){
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                Integer count=0;
                @Override
                public void run() {
                    if(count==5&&ctx==null){
                        content.changeSendStatus(Message.FAILED);
                        timer.cancel();
                    }
                    else if(ctx!=null){
                        content.changeSendStatus(Message.SENT);
                        ctx.writeAndFlush(content);
                        timer.cancel();
                    }
                    ++count;
                }
            };
            timer.scheduleAtFixedRate(task, 0, 1000);
        }
    }
    public void getLoginStatus(Integer receiverId, ChannelHandlerContext ctx) {
        ctx.writeAndFlush(new LoginStatusRequestMessage(receiverId));
    }

    public BufferedImage getAvatar(String avatarPath) {
        try {
            BufferedImage image1;
            File avatar = new File(avatarPath);
            if (!avatar.exists()) {
                image1 = ImageIO.read(new File("img.png"));
            }else image1= ImageIO.read(avatar);
            return image1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将用户接收到的信息写入数据库
     * @param masterId
     * @param senderId
     * @param receiverId
     * @param content
     */
    public void persistentMessage(Integer masterId,Integer senderId,Integer receiverId,String content){

        ThreadPoolManager.getDBExecutorService().execute(
                ()->private_chat_recordsMapper.insertSingleMessage(masterId,senderId,receiverId,content)
        );
    }
}
