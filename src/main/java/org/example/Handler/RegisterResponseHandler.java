package org.example.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.Controller.LoginController;
import org.example.Model.message.responseMessage.RegisterRequestResponseMessage;


public class RegisterResponseHandler extends SimpleChannelInboundHandler<RegisterRequestResponseMessage> {
    private LoginController loginController;

    public RegisterResponseHandler(LoginController loginController) {
        this.loginController = loginController;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,RegisterRequestResponseMessage registerRequestResponseMessager) throws Exception {
        if (registerRequestResponseMessager.isSuccess()) {
            loginController.showMessage("注册成功，新注册的账号为:\n" + registerRequestResponseMessager.getReason());
        } else {
            loginController.showMessage(registerRequestResponseMessager.getReason());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }


}
