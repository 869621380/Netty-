package org.example.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.Cache.MessageCache;
import org.example.Controller.ChatWindowMessageController;
import org.example.Model.Domain.SingleChatMessage;
import org.example.Model.message.requestMessage.SingleChatImageRequestMessage;
import org.example.Model.message.requestMessage.SingleChatRequestMessage;
import org.example.Model.message.requestMessage.SingleChatTextRequestMessage;
import org.example.View.MainFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SingleChatRequestHandler extends SimpleChannelInboundHandler<SingleChatRequestMessage> {

    private static final Logger log = LoggerFactory.getLogger(SingleChatRequestHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, SingleChatRequestMessage singleChatRequestMessage) throws Exception {
        ChatWindowMessageController chatWindowMessageController
                =MessageCache.GetChatWindowMessageControllerMap().get(singleChatRequestMessage.getSenderID());
        if(chatWindowMessageController==null)return;
        SingleChatMessage singleChatMessage=
                new SingleChatMessage(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")),
                        SingleChatMessage.SENDING, singleChatRequestMessage.getSenderID(),
                        singleChatRequestMessage.getReceiverID(),null,null);
        if(singleChatRequestMessage instanceof SingleChatTextRequestMessage){
            singleChatMessage.setType("text");
            singleChatMessage.setContent(((SingleChatTextRequestMessage)singleChatRequestMessage).getContent());
            MessageCache.getChatListController().updatePreview(singleChatMessage.getSenderID(),(String) singleChatMessage.getContent());
        }

        else if(singleChatRequestMessage instanceof SingleChatImageRequestMessage){
            log.debug("收到了一张图片");
            singleChatMessage.setType("image");
            singleChatMessage.setContent(((SingleChatImageRequestMessage)singleChatRequestMessage).getContent());
            MessageCache.getChatListController().updatePreview(singleChatMessage.getSenderID(),"[图片]");
        }
        chatWindowMessageController.receiveMessage(singleChatMessage);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("channelInactive method called");
        MainFrame mainFrame=MessageCache.getMainFrameCache();
        if(mainFrame!=null&&!mainFrame.isCtx()){
            mainFrame.addCtx(ctx);
        }
        log.debug("添加连接");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("channelInactive method called");
        MainFrame mainFrame=MessageCache.getMainFrameCache();
        if(mainFrame!=null&&mainFrame.isCtx()){
            mainFrame.removeCtx();
        }
    }

}
