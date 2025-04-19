package org.example.Controller;

import io.netty.channel.ChannelHandlerContext;
import lombok.Setter;
import org.example.Cache.MessageCache;
import org.example.Model.Domain.Message;
import org.example.Model.Domain.SingleChatMessage;
import org.example.Model.Domain.UserInfo;
import org.example.Model.message.requestMessage.SingleChatImageRequestMessage;
import org.example.Model.message.requestMessage.SingleChatRequestMessage;
import org.example.Model.message.requestMessage.SingleChatTextRequestMessage;
import org.example.Service.ChatMessageService;
import org.example.Service.UserInfoService;
import org.example.View.ChatWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatWindowMessageController implements ChatWindow.ChatMessageListener {

    private static final Logger log = LoggerFactory.getLogger(ChatWindowMessageController.class);
    private  ChatWindow view;
    private final ChatMessageService chatMessageService;
    private final UserInfoService userInfoService;

    @Setter
    ChannelHandlerContext ctx;

    public ChatWindowMessageController(ChatWindow view) {
        ctx=null;
        this.view = view;
        chatMessageService=new ChatMessageService();
        userInfoService=new UserInfoService();
        if(view!=null) view.setChatWindowMessageListener(this);
    }

    public void setView(ChatWindow view) {
        this.view=view;
        if(view!=null) view.setChatWindowMessageListener(this);

    }

    @Override
    public void setInitData(Integer senderId, Integer receiverId) {

        List<UserInfo>userInfos= userInfoService.getUserAvatar(senderId, receiverId);
        view.setReceiverNameLabel(userInfos.get(1).getNickname());
        view.setStatusLabel("未知");
        //加载头像
        try {
            BufferedImage image1,image2;
            File avatar = new File(userInfos.get(0).getAvatarPath());
            if (!avatar.exists()) {
                image1 = ImageIO.read(new File("img.png"));
            }else image1= ImageIO.read(avatar);
            File avatar2 = new File(userInfos.get(1).getAvatarPath());
            if (!avatar2.exists()) {
                image2 = ImageIO.read(new File("img.png"));
            }else image2= ImageIO.read(avatar2);
            view.setAvatar(image1, image2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //加载本地数据
        List<SingleChatMessage>singleChatMessages=chatMessageService.getSingleChatTextMessage(senderId,receiverId);

        for(SingleChatMessage singleChatMessage:singleChatMessages){
            view.addMessage(singleChatMessage);
        }

        view.revalidate();
        view.repaint();
    }

    @Override
    public void sendMessage(SingleChatMessage content) {
        SingleChatRequestMessage singleChatRequestMessage = null;
        if(content.getType().equals("text")){
            singleChatRequestMessage=new SingleChatTextRequestMessage(content.getSendTime(),content.getSenderID(),content.getReceiverID(),(String) content.getContent());
            MessageCache.getChatListController().updatePreview(content.getReceiverID(), (String) content.getContent());
        }
        else if(content.getType().equals("image")){

            singleChatRequestMessage=new SingleChatImageRequestMessage(content.getSendTime(),content.getSenderID(),content.getReceiverID(),(byte[])content.getContent());
            log.debug("HERE WILL SEND IMAGE");
            MessageCache.getChatListController().updatePreview(content.getReceiverID(),"[图片]");
        }

        if(ctx!=null&&singleChatRequestMessage!=null){
            MessageCache.addMessageCache(singleChatRequestMessage.getSequenceId(),content);
            //这里应该响应后再SENT
            content.changeSendStatus(Message.SENT);
            ctx.writeAndFlush(singleChatRequestMessage);
            log.debug("HERE SEND MESSAGE"+singleChatRequestMessage.getMessageType());
        }
    }

    public void receiveMessage(SingleChatMessage singleChatMessage) {
        view.addMessage(singleChatMessage);
    }


}
