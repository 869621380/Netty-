package org.example.Controller;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;
import org.example.Model.Domain.ChatItem;
import org.example.Model.Domain.FriendInfo;
import org.example.Service.ChatListService;
import org.example.Service.FriendService;
import org.example.Util.ThreadPoolManager;
import org.example.View.ChatListPanel;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ChatListController implements ChatListPanel.ChatListListener {
    private final ChatListPanel view;
    private final ChatListService chatListService;
    @Getter
    private CountDownLatch latch = new CountDownLatch(1);
    @Setter
    ChannelHandlerContext ctx;
    public ChatListController(ChatListPanel view) {
        this.view = view;
        chatListService=new ChatListService();
        view.setChatListener(this);
    }

    @Override
    public void setInitData(Integer userId) {
        List<ChatItem>chatItems=chatListService.getChatItems(userId);
        view.addChatItem(chatItems);
        view.revalidate();
        view.repaint();
        ThreadPoolManager.getDBExecutorService().execute(() -> {
            view.addChatWindow(chatItems);
            latch.countDown();
        });

    }

    public void updatePreview(Integer receiverId,String content) {
        view.updateItem(receiverId,content);

        view.revalidate();
        view.repaint();
    }

    public void closeCtx() {
        if(ctx!=null) {
            ctx.close();
        }
    }

    public void refreshFriendList() {
        // 从数据库或缓存中获取最新的好友列表
        ThreadPoolManager.getDBExecutorService().execute(() -> {
            try {
                // 使用FriendService获取好友列表
                List<FriendInfo> friendList = new FriendService().getAllFriends();

                // 在UI线程中更新视图
                SwingUtilities.invokeLater(() -> {
                    // 使用已有的view对象更新好友列表
                    view.updateFriendList(friendList);

                    // 刷新UI
                    view.revalidate();
                    view.repaint();
                });
            } catch (Exception e) {
                // 使用适当的日志对象
                System.err.println("刷新好友列表失败: " + e.getMessage());
            }
        });
    }
}
