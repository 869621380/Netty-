package org.example.Controller;

import io.netty.channel.ChannelHandlerContext;
import lombok.Setter;

import org.example.Handler.UserInfoResponseHandler;
import org.example.Model.Domain.GroupChatMessage;
import org.example.Model.Domain.Message;
import org.example.Model.Domain.SingleChatMessage;
import org.example.Model.Domain.UserInfo;
import org.example.Model.message.requestMessage.SingleChatTextRequestMessage;
import org.example.Service.ChatMessageService;
import org.example.Service.UserInfoService;
import org.example.Util.ThreadPoolManager;
import org.example.View.ChatWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
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
        if(view!=null) {
            view.setChatWindowMessageListener(this);

            // 注册到处理器
            UserInfoResponseHandler.registerController(view.getReceiverId(), this);
        }
    }
    public void setView(ChatWindow view) {
        this.view=view;
        if(view!=null) view.setChatWindowMessageListener(this);
    }

    // @Override
    // public void setInitData(Integer senderId, Integer receiverId) {

    //     List<UserInfo>userInfos= userInfoService.getUserAvatar(senderId, receiverId);

    //     view.setReceiverNameLabel(userInfos.get(1).getNickname());
    //     view.setStatusLabel("未知");
    //     //加载头像
    //     BufferedImage image1=chatMessageService.getAvatar(userInfos.get(0).getAvatarPath());
    //     BufferedImage image2=chatMessageService.getAvatar(userInfos.get(1).getAvatarPath());
    //     view.setAvatar(image1,image2);
    //     //加载本地数据
    //     List<SingleChatMessage>singleChatMessages=chatMessageService.getSingleChatTextMessage(senderId,receiverId);
    //     for(SingleChatMessage singleChatMessage:singleChatMessages){
    //         view.addMessage(singleChatMessage);
    //     }
    //     view.revalidate();
    //     view.repaint();
    // }

    @Override
    public void setInitData(Integer senderId, Integer receiverId) {
    // 获取用户信息
        List<UserInfo> userInfos = userInfoService.getUserAvatar(senderId, receiverId);

    // 检查列表长度并安全访问
        if (userInfos != null && userInfos.size() >= 2) {
        // 原有逻辑 - 有两个用户信息时
            view.setReceiverNameLabel(userInfos.get(1).getNickname());
            view.setStatusLabel("未知");

        // 加载头像
            BufferedImage image1 = chatMessageService.getAvatar(userInfos.get(0).getAvatarPath());
            BufferedImage image2 = chatMessageService.getAvatar(userInfos.get(1).getAvatarPath());
            view.setAvatar(image1, image2);
        } else {
        // 处理列表不完整的情况
        // 1. 如果至少有一个用户信息
            if (userInfos != null && userInfos.size() == 1) {
            // 可能是当前用户
                BufferedImage image1 = chatMessageService.getAvatar(userInfos.get(0).getAvatarPath());

            // 为接收者创建默认信息
                view.setReceiverNameLabel("用户" + receiverId);
                view.setStatusLabel("未知");

            // 为接收者创建默认头像
                BufferedImage defaultImage = createDefaultAvatar(receiverId);
                view.setAvatar(image1, defaultImage);
            } else {
            // 完全没有用户信息的情况
                view.setReceiverNameLabel("用户" + receiverId);
                view.setStatusLabel("未知");

            // 创建默认头像
                BufferedImage defaultSenderImage = createDefaultAvatar(senderId);
                BufferedImage defaultReceiverImage = createDefaultAvatar(receiverId);
                view.setAvatar(defaultSenderImage, defaultReceiverImage);
            }

        // 可能需要重新获取用户信息
            if (ctx != null) {
            // 向服务器请求用户信息 - 您可能需要实现这个方法
                userInfoService.requestUserInfo(senderId, receiverId, ctx);
            }
        }

    // 加载本地聊天记录
        List<SingleChatMessage> singleChatMessages = chatMessageService.getSingleChatTextMessage(senderId, receiverId);
        for (SingleChatMessage singleChatMessage : singleChatMessages) {
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

// 添加创建默认头像的辅助方法
    private BufferedImage createDefaultAvatar(Integer userId) {
        int width = 40;
        int height = 40;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();

    // 根据用户ID生成颜色
        int r = (userId * 123) % 256;
        int g = (userId * 255) % 256;
        int b = (userId * 189) % 256;
        g2d.setColor(new Color(r, g, b));
        g2d.fillRect(0, 0, width, height);

    // 添加用户ID作为标识
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        String label = String.valueOf(userId);
        FontMetrics fm = g2d.getFontMetrics();
        int strWidth = fm.stringWidth(label);
        int strHeight = fm.getHeight();
        g2d.drawString(label, (width - strWidth) / 2, height / 2 + strHeight / 4);

        g2d.dispose();
        return img;
    }

        // 添加新方法更新用户信息
    public void updateUserInfo(UserInfo userInfo) {
        if (userInfo == null || view == null) return;

        SwingUtilities.invokeLater(() -> {
            // 更新名称
            view.setReceiverNameLabel(userInfo.getNickname());

            // 更新头像
            if (userInfo.getAvatarPath() != null && !userInfo.getAvatarPath().isEmpty()) {
                BufferedImage avatar = chatMessageService.getAvatar(userInfo.getAvatarPath());
                if (avatar != null) {
                    // 获取当前发送者头像
                    BufferedImage senderAvatar = view.getSenderAvatar();
                    if (senderAvatar != null) {
                        view.setAvatar(senderAvatar, avatar);
                    }
                }
            }

            view.revalidate();
            view.repaint();
        });
    }

    // 在视图关闭时取消注册
    public void cleanup() {
        if (view != null) {
            UserInfoResponseHandler.unregisterController(view.getReceiverId());
        }
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


    public void moveToBottom(){
        view.moveToBottom();
    }
}
