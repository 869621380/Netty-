package org.example.Server.Handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.example.Model.message.PingMessage;
import org.example.Model.message.PongMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ServerHeartbeatHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger log = LoggerFactory.getLogger(ServerHeartbeatHandler.class);
    private static final PingMessage HEARTBEAT_PACKET = new PingMessage();
    private static final PongMessage PONG_MESSAGE = new PongMessage();
    private static final int PONG_TIMEOUT_SECONDS = 3; // 等待 Pong 消息的超时时间
    private ChannelHandlerContext ctx;
    private ChannelFuture timeoutFuture;
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                // 当读空闲时，服务端可以主动发送 Ping 消息检测连接
                log.info("Send heartbeat packet (Ping) to client");
                ctx.writeAndFlush(HEARTBEAT_PACKET);
                // 启动超时定时器
                timeoutFuture = (ChannelFuture) ctx.executor().schedule(() -> {
                    log.error("Pong message not received from client within timeout period, closing connection.");
                    ctx.close();
                }, PONG_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info(msg.getClass().getName());
        if (msg.getClass().equals(PingMessage.class)) {
            log.info("Received Ping message from client, send Pong message");
            ctx.writeAndFlush(PONG_MESSAGE);
        } else if (msg.getClass().equals(PongMessage.class)) {
            log.info("Received Pong message from client");
            // 收到 Pong 消息，取消超时定时器
            if (timeoutFuture != null) {
                timeoutFuture.cancel(false);
            }
        } else {
            // 如果不是 Ping 或 Pong 消息，传递给下一个处理器处理
            ctx.fireChannelRead(msg);
        }
    }
}
