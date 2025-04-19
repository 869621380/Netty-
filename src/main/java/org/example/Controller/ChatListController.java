package org.example.Controller;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;
import org.example.Model.Domain.ChatItem;
import org.example.Model.Domain.SingleChatMessage;
import org.example.Service.ChatListService;
import org.example.View.ChatListPanel;

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
        new Thread(()-> {
            view.addChatWindow(chatItems);
            latch.countDown();
        }).start();
    }

    public void updatePreview(Integer receiverId,String content) {
        view.updateItem(receiverId,content);

        view.revalidate();
        view.repaint();
    }

}
