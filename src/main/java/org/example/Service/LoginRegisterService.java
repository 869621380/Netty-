package org.example.Service;

import io.netty.channel.ChannelHandlerContext;
import lombok.NoArgsConstructor;
import org.example.Model.message.requestMessage.LoginRequestMessage;
import org.example.Model.message.requestMessage.RegisterCodeRequestMessage;
import org.example.Model.message.requestMessage.RegisterRequestMessage;
import org.example.View.LoginView;

@NoArgsConstructor
public class LoginRegisterService {



    public void login(LoginRequestMessage event, ChannelHandlerContext ctx) {

        LoginRequestMessage request = new LoginRequestMessage(event.getUserId(), event.getPassword());
        ctx.writeAndFlush(request);


    }

    public void register(RegisterRequestMessage message, ChannelHandlerContext ctx) {
        ctx.writeAndFlush(message);
    }

    public void getCode(RegisterCodeRequestMessage registerCodeRequestMessage, ChannelHandlerContext ctx) {
        ctx.writeAndFlush(registerCodeRequestMessage);
    }
}
