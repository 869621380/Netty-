package org.example.Server.Handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.Model.message.requestMessage.SingleChatRequestMessage;
import org.example.Server.Session.SessionFactory;

public class SingleChatMessageHandler extends SimpleChannelInboundHandler<SingleChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, SingleChatRequestMessage singleChatRequestMessageMessage) throws Exception {
        Integer receiverID = singleChatRequestMessageMessage.getReceiverID();
        Channel receiverChannel = SessionFactory.getSession().getChannel(receiverID);
        if(receiverChannel != null) {
            receiverChannel.writeAndFlush(singleChatRequestMessageMessage);
        }
    }
}
