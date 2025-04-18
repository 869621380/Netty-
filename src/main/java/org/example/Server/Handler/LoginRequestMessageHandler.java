package org.example.Server.Handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.Model.message.requestMessage.LoginRequestMessage;
import org.example.Model.message.responseMessage.LoginRequestResponseMessage;
import org.example.Server.Session.SessionFactory;
import org.example.Util.Factory.LoginServiceFactory;

@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        System.out.println("LoginRequestMessageHandler");
        Integer userId = msg.getUserId();
        String password = msg.getPassword();
        boolean login = LoginServiceFactory.getUserService().login(userId, password);
        LoginRequestResponseMessage message;
        if (login) {
            SessionFactory.getSession().bind(ctx.channel(), userId);
            message = new LoginRequestResponseMessage(true, Integer.toString(userId));
        } else {
            message = new LoginRequestResponseMessage(false, "用户名或密码不正确");
        }
        ctx.writeAndFlush(message);

    }
}

