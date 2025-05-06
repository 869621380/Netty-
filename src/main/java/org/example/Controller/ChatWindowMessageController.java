package org.example.Controller;

import io.netty.channel.ChannelHandlerContext;
import lombok.Setter;
import org.example.Model.Domain.GroupChatMessage;
import org.example.Model.Domain.Message;
import org.example.Model.Domain.SingleChatMessage;
import org.example.Model.Domain.UserInfo;
import org.example.Service.ChatMessageService;
import org.example.Service.UserInfoService;
import org.example.View.ChatWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
        System.out.println("发送人和接收人ID："+senderId+"  "+receiverId);
        List<UserInfo>userInfos= userInfoService.getUserAvatar(senderId, receiverId);
        System.out.println(userInfos);
        view.setReceiverNameLabel(userInfos.get(1).getNickname());
        view.setStatusLabel("未知");
        //加载头像
        BufferedImage image1=chatMessageService.getAvatar(userInfos.get(0).getAvatarPath());
        BufferedImage image2=chatMessageService.getAvatar(userInfos.get(1).getAvatarPath());
        view.setAvatar(image1,image2);
        //加载本地数据
        List<SingleChatMessage>singleChatMessages=chatMessageService.getSingleChatTextMessage(senderId,receiverId);
        for(SingleChatMessage singleChatMessage:singleChatMessages){
            view.addMessage(singleChatMessage);
        }
        view.revalidate();
        view.repaint();
    }

    @Override
    public void setGroupInitData(Integer senderId, String receiverName) {
        System.out.println("群聊初始化入口");
        System.out.println("发送人和接收人ID："+senderId+"  "+receiverName);
        //List<UserInfo>userInfos= userInfoService.getUserAvatar(senderId);
        //System.out.println(userInfos);
        view.setReceiverNameLabel(receiverName);
        view.setStatusLabel("");
        //加载头像
        BufferedImage image1=chatMessageService.getAvatar("img.png");
        BufferedImage image2=chatMessageService.getAvatar("img.png");
        view.setAvatar(image1,image2);
        //加载本地数据,带发送人名字
        List<GroupChatMessage>groupChatMessages=chatMessageService.getGroupChatTextMessage(receiverName);
        for(GroupChatMessage groupChatMessage:groupChatMessages){
            view.addMessage(groupChatMessage);

        }
        view.revalidate();
        view.repaint();
    }

    @Override
    public void sendMessage(SingleChatMessage content) {
        chatMessageService.sendMessage(content,ctx);
    }

    @Override
    public void sendGroupMessage(GroupChatMessage content) {
        chatMessageService.sendGroupMessage(content,ctx);
    }

    @Override
    public void flushLoginStatus(Integer receiverId) {
        if(ctx==null){
            view.setStatusLabel("未知");
        }
        //有网就向服务器发请求
        else chatMessageService.getLoginStatus(receiverId,ctx);
    }

    @Override
    public void getReceiverLoginStatus(Integer receiverId) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                flushLoginStatus(receiverId);
            }
        };
        timer.scheduleAtFixedRate(task, 0, 10000);
    }

    public void receiveMessage(Message ChatMessage) {
        //处理收到的数据，为senderName赋值
        if(ChatMessage instanceof GroupChatMessage) {
            ChatMessage.senderName = userInfoService.getNameById(ChatMessage.getSenderID());
        }
        view.addMessage(ChatMessage);
    }

    public void setLoginStatus(String loginStatus) {
        view.setStatusLabel(loginStatus);
    }


}
