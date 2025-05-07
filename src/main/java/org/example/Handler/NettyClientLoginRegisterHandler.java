package org.example.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.Cache.MessageCache;
import org.example.Controller.LoginController;
import org.example.Model.message.responseMessage.LoginRequestResponseMessage;

import org.example.View.MainFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class NettyClientLoginRegisterHandler extends SimpleChannelInboundHandler<LoginRequestResponseMessage> {
    private static final Logger log = LoggerFactory.getLogger(NettyClientLoginRegisterHandler.class);
    private LoginController loginController;

    public NettyClientLoginRegisterHandler(LoginController loginController) {
        this.loginController = loginController;
    }
    @Override
    public void channelRead0(ChannelHandlerContext ctx, LoginRequestResponseMessage msg) throws Exception {

        LoginRequestResponseMessage response = (LoginRequestResponseMessage) msg;
        log.debug("response: {}", response);
        if(response.isSuccess()){
            LoginController.setToken(response.getToken());
            loginController.closeLoginView();
            SwingUtilities.invokeLater(() -> {
                MainFrame mainFrame=new MainFrame(Integer.parseInt(response.getReason()));
                MessageCache.setMainFrameCache(mainFrame);
                mainFrame.setVisible(true);
                mainFrame.addCtx(ctx);
            });

        }
        else loginController.showMessage(response.getReason());

    }

    // 在连接建立后触发 active 事件
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("登录连接建立");
        // 负责接收用户在控制台的输入，负责向服务器发送各种消息
        loginController.setCtx(ctx);
        ctx.fireChannelActive();
    }

    // 在连接断开时触发
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        loginController.setCtx(null);
        log.debug("登录连接已经断开");
        ctx.fireChannelInactive();
    }

    // 在出现异常时触发
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        loginController.setCtx(null);
        log.debug("连接异常断开{}", cause.getMessage());
    }
}
