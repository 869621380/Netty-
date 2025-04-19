package org.example.Server.Handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.Model.message.requestMessage.SingleChatRequestMessage;
import org.example.Server.Session.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleChatMessageHandler extends SimpleChannelInboundHandler<SingleChatRequestMessage> {
    private static final Logger log = LoggerFactory.getLogger(SingleChatMessageHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, SingleChatRequestMessage singleChatRequestMessage) throws Exception {
        Integer receiverID = singleChatRequestMessage.getReceiverID();
        Channel receiverChannel = SessionFactory.getSession().getChannel(receiverID);
        log.debug("RECEIVER ID: " + receiverID);
        if(receiverChannel != null) {
            receiverChannel.writeAndFlush(singleChatRequestMessage);
            log.debug("处理了一条请求");
        }
    }
}
