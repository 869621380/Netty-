package org.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.example.Controller.LoginController;
import org.example.Handler.*;

import org.example.Model.message.IdentityVerifyMessage;
import org.example.Model.proto.MessageCodec;
import org.example.Model.proto.ProtoFrameDecoder;

import org.example.View.LoginView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.example.Util.Constants.SERVER_HOST;
import static org.example.Util.Constants.SERVER_PORT;

public class Client {
    private static final Logger log = LoggerFactory.getLogger(Client.class);

    private static final int RECONNECT_DELAY = 1; // 重连间隔时间（秒）

    public static void main(String[] args) {
        LoginView loginView = new LoginView();
        LoginController loginController = new LoginController(loginView);
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(group);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new IdleStateHandler(0, 15, 0));
                ch.pipeline().addLast(new ProtoFrameDecoder());
                ch.pipeline().addLast(new MessageCodec());
                ch.pipeline().addLast(new HeartbeatAndPongHandler());
                ch.pipeline().addLast(new NettyClientLoginRegisterHandler(loginController));
                //获取用户在线信息
                ch.pipeline().addLast(new LoginStatusResponseHandler());
                ch.pipeline().addLast(new SingleChatRequestHandler());

                ch.pipeline().addLast(new GroupCreateResponseHandler());
                ch.pipeline().addLast(new GroupChatTextResponseHandler());
                ch.pipeline().addLast(new GroupChatImageResponseHandler());
            }
        });

        try {
            connect(bootstrap, SERVER_HOST, SERVER_PORT, group);
            // 保持主线程存活，避免过早退出
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            log.error("Main thread interrupted", e);
        } finally {
            group.shutdownGracefully();
        }
    }

    private static void connect(Bootstrap bootstrap, String host, int port, NioEventLoopGroup group) {
        bootstrap.connect(host, port).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("Connected to server: {}:{}", host, port);
                Channel channel = future.channel();
                // 获取存储的身份标识
                String token = LoginController.getToken();
                if (token!= null) {
                    // 发送身份标识给服务器进行验证
                    IdentityVerifyMessage identityVerifyMessage = new IdentityVerifyMessage(token);
                    channel.writeAndFlush(identityVerifyMessage);
                }
                channel.closeFuture().addListener((ChannelFutureListener) closeFuture -> {
                    log.info("Connection closed, trying to reconnect...");
                    scheduleReconnect(bootstrap, host, port, group);
                });
            } else {
                log.error("Failed to connect to server: {}:{}, retrying in {} seconds...", host, port, RECONNECT_DELAY);
                scheduleReconnect(bootstrap, host, port, group);
            }
        });
    }

    private static void scheduleReconnect(Bootstrap bootstrap, String host, int port, NioEventLoopGroup group) {
        group.schedule(() -> connect(bootstrap, host, port, group), RECONNECT_DELAY, java.util.concurrent.TimeUnit.SECONDS);
    }
}