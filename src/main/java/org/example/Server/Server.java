package org.example.Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.example.Model.proto.MessageCodec;
import org.example.Model.proto.ProtoFrameDecoder;
import org.example.Server.Handler.GlobalExceptionHandler;
import org.example.Server.Handler.LoginRequestMessageHandler;
import org.example.Server.Handler.ServerHeartbeatHandler;
import org.example.Server.Handler.SingleChatMessageHandler;

public class Server {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.group(bossGroup, workerGroup);

        Channel channel=bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override protected
            void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new IdleStateHandler(5, 0, 0));

                ch.pipeline().addLast(new ProtoFrameDecoder());
                ch.pipeline().addLast(new MessageCodec());
                ch.pipeline().addLast(new ServerHeartbeatHandler());
                ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                ch.pipeline().addLast(new LoginRequestMessageHandler());
                ch.pipeline().addLast(new SingleChatMessageHandler());
                ch.pipeline().addLast(new GlobalExceptionHandler());
            }
        }

        ).bind(8080).syncUninterruptibly().channel();

        try {
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }

    }
}
