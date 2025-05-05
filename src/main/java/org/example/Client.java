package org.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.example.Controller.LoginController;
import org.example.Handler.*;

//import org.example.Model.Domain.Message;
import org.example.Model.message.Message;
import org.example.Model.Domain.UserInfo;
import org.example.Model.message.IdentityVerifyMessage;
import org.example.Model.proto.MessageCodec;
import org.example.Model.proto.ProtoFrameDecoder;

import org.example.View.LoginView;
import org.example.View.MainFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

import static org.example.Util.Constants.SERVER_HOST;
import static org.example.Util.Constants.SERVER_PORT;

public class Client {
    private static final Logger log = LoggerFactory.getLogger(Client.class);

    private static final int RECONNECT_DELAY = 1; // 重连间隔时间（秒）


    // 单例模式实现
    private static volatile Client instance;

    // 当前登录用户信息
    private UserInfo currentUser;

    // 通信通道
    private Channel channel;

    // 私有构造函数，防止外部直接实例化
    private Client() { }

    // 获取单例实例的方法
    public static Client getInstance() {
        if (instance == null) {
            synchronized (Client.class) {
                if (instance == null) {
                    instance = new Client();
                }
            }
        }
        return instance;
    }

    // 获取当前用户信息
    public UserInfo getCurrentUser() {
        return currentUser;
    }

    // 设置当前用户信息
    public void setCurrentUser(UserInfo user) {
        this.currentUser = user;
    }

    // 获取通信通道
    public Channel getChannel() {
        return channel;
    }

    // 设置通信通道
    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    // 在Client的sendMessage方法中添加日志
    public void sendMessage(Message message) {
        if (channel != null && channel.isActive()) {
            log.debug("发送消息: type={}, seq={}", message.getMessageType(), message.getSequenceId());
            channel.writeAndFlush(message);
        } else {
            log.error("无法发送消息，通道未连接");
        }
    }

    public boolean isConnected() {
        return channel != null && channel.isActive();
    }

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

                // 添加FriendRequestHandler
                ch.pipeline().addLast(new FriendRequestHandler());

                // 添加GroupCreateHandler
                ch.pipeline().addLast(new GroupCreateHandler());
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

//    private static void connect(Bootstrap bootstrap, String host, int port, NioEventLoopGroup group) {
//        bootstrap.connect(host, port).addListener((ChannelFutureListener) future -> {
//            if (future.isSuccess()) {
//                log.info("Connected to server: {}:{}", host, port);
//                Channel channel = future.channel();
//                // 获取存储的身份标识
//                String token = LoginController.getToken();
//                if (token!= null) {
//                    // 发送身份标识给服务器进行验证
//                    IdentityVerifyMessage identityVerifyMessage = new IdentityVerifyMessage(token);
//                    channel.writeAndFlush(identityVerifyMessage);
//                    // 重连成功后恢复UI状态
//                    SwingUtilities.invokeLater(() -> {
//                        MainFrame mainFrame = MainFrame.getInstance();
//                        if (mainFrame != null) {
//                            mainFrame.addCtx(channel.pipeline().lastContext());
//                            mainFrame.reconnectUI();  // 添加这个新方法
//                        }
//                    });
//                }
//                channel.closeFuture().addListener((ChannelFutureListener) closeFuture -> {
//                    log.info("Connection closed, trying to reconnect...");
//                    scheduleReconnect(bootstrap, host, port, group);
//                });
//            } else {
//                log.error("Failed to connect to server: {}:{}, retrying in {} seconds...", host, port, RECONNECT_DELAY);
//                scheduleReconnect(bootstrap, host, port, group);
//            }
//        });
//    }

    private static void connect(Bootstrap bootstrap, String host, int port, NioEventLoopGroup group) {
        bootstrap.connect(host, port).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("Connected to server: {}:{}", host, port);
                Channel channel = future.channel();
                // 获取存储的身份标识
                Client.getInstance().setChannel(channel);

                String token = LoginController.getToken();
                if (token != null) {
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