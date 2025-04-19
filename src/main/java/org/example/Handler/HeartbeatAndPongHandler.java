package org.example.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.example.Model.message.PingMessage;
import org.example.Model.message.PongMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeartbeatAndPongHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger log = LoggerFactory.getLogger(HeartbeatAndPongHandler.class);
    private static final PingMessage HEARTBEAT_PACKET = new PingMessage();
    private static final PongMessage PONG_MESSAGE = new PongMessage();
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                // 当写空闲时，发送心跳包
                log.info("Send heartbeat packet (Ping)");
                ctx.writeAndFlush(HEARTBEAT_PACKET);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg.getClass().equals(PongMessage.class)) {
            log.info("Received Pong message from server");
        } else if (msg.getClass().equals(PingMessage.class)) {
            log.info("Received Ping message from server, send Pong message");
            ctx.writeAndFlush(PONG_MESSAGE);
        } else {
            // 如果不是 Pong 或 Ping 消息，传递给下一个处理器处理
            ctx.fireChannelRead(msg);
        }
    }
}