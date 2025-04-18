package org.example.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.Cache.MessageCache;
import org.example.Controller.ChatListController;
import org.example.Controller.ChatWindowMessageController;
import org.example.Model.Domain.SingleChatMessage;
import org.example.Model.message.requestMessage.SingleChatRequestMessage;
import org.example.Model.message.requestMessage.SingleChatTextRequestMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SingleChatRequestHandler extends SimpleChannelInboundHandler<SingleChatRequestMessage> {

    private static final Logger log = LoggerFactory.getLogger(SingleChatRequestHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, SingleChatRequestMessage singleChatRequestMessage) throws Exception {
        if(singleChatRequestMessage instanceof SingleChatTextRequestMessage){
            ChatWindowMessageController chatWindowMessageController
                    =MessageCache.GetChatWindowMessageControllerMap().get(singleChatRequestMessage.getSenderID());
            if(chatWindowMessageController!=null){
                SingleChatMessage singleChatMessage=
                        new SingleChatMessage(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")),
                                SingleChatMessage.SENDING, singleChatRequestMessage.getSenderID(),
                                singleChatRequestMessage.getReceiverID(),"text",((SingleChatTextRequestMessage) singleChatRequestMessage).getContent());
                log.debug(singleChatMessage.toString());
                chatWindowMessageController.receiveMessage(singleChatMessage);
            }
        }
    }
}
