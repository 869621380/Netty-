package org.example.Client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.example.Controller.LoginController;
import org.example.Handler.NettyClientLoginRegister;
import org.example.Handler.SingleChatRequestHandler;
import org.example.Model.message.responseMessage.LoginRequestResponseMessage;
import org.example.Model.proto.MessageCodec;
import org.example.Model.proto.ProtoFrameDecoder;
import org.example.View.LoginView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Client {
    private static final Logger log = LoggerFactory.getLogger(Client.class);
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;
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
                ch.pipeline().addLast(new IdleStateHandler(0, 1, 0));

                ch.pipeline().addLast(new ProtoFrameDecoder());
                ch.pipeline().addLast(new MessageCodec());
                ch.pipeline().addLast(new NettyClientLoginRegister(loginController));
                ch.pipeline().addLast(new SingleChatRequestHandler());
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