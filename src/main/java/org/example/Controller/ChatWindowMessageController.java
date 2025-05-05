package org.example.Controller;

import io.netty.channel.ChannelHandlerContext;
import lombok.Setter;
import org.example.Model.Domain.SingleChatMessage;
import org.example.Model.Domain.UserInfo;
import org.example.Service.ChatMessageService;
import org.example.Service.UserInfoService;
import org.example.View.ChatWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

//    public ChatWindowMessageController(ChatWindow view) {
//        ctx=null;
//        this.view = view;
//        chatMessageService=new ChatMessageService();
//        userInfoService=new UserInfoService();
//        if(view!=null) view.setChatWindowMessageListener(this);
//    }
//
//    public void setView(ChatWindow view) {
//        this.view=view;
//        if(view!=null) view.setChatWindowMessageListener(this);
//    }

    public ChatWindowMessageController(ChatWindow view) {
        ctx = null;
        this.view = view;
        chatMessageService = new ChatMessageService();
        userInfoService = new UserInfoService();
        if(view != null) {
            view.setChatWindowMessageListener(this);  // 此处缺失了调用方法
        }
    }

    public void setView(ChatWindow view) {
        this.view = view;
        if(view != null) {
            view.setChatWindowMessageListener(this);  // 此处缺失了调用方法
        }
    }

    @Override
    public void setInitData(Integer senderId, Integer receiverId) {

        List<UserInfo>userInfos= userInfoService.getUserAvatar(senderId, receiverId);
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
    public void sendMessage(SingleChatMessage content) {
        chatMessageService.sendMessage(content,ctx);
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

    public void receiveMessage(SingleChatMessage singleChatMessage) {
        view.addMessage(singleChatMessage);
    }

    public void setLoginStatus(String loginStatus) {
        view.setStatusLabel(loginStatus);
    }

//    public void setCtx(ChannelHandlerContext ctx) {
//        this.ctx = ctx;
//
//        // 当设置新的ctx时，如果窗口当前可见，自动刷新消息
//        if (window != null && window.isVisible()) {
//            loadMessages();
//        }
//
//        logger.debug("已设置ChannelHandlerContext: {}", ctx != null);
//    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;

        // 更新状态
        if (view != null && view.isVisible() && ctx != null) {
            // 获取接收者ID
            try {
                // 假设可以通过某种方式获取接收者ID
                Integer receiverId = view.getReceiverId(); // 或通过其他方式获取
                if (receiverId != null) {
                    // 刷新登录状态
                    flushLoginStatus(receiverId);
                }
            } catch (Exception e) {
                log.error("刷新状态失败", e);
            }
        }

        log.debug("已设置ChannelHandlerContext: {}", ctx != null);
    }

}
