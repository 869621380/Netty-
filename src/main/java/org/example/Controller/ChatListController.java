package org.example.Controller;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;
import org.example.Model.Domain.ChatItem;
import org.example.Service.ChatListService;
import org.example.Util.ThreadPoolManager;
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
}
