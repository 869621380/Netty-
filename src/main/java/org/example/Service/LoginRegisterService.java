package org.example.Service;

import io.netty.channel.ChannelHandlerContext;
import org.example.Model.message.requestMessage.LoginAndRegisterRequestMessage;
import org.example.Model.message.requestMessage.LoginRequestMessage;
import org.example.Model.message.requestMessage.RegisterRequestMessage;
import org.example.View.LoginView;

public class LoginRegisterService {
    private LoginView view;

    public LoginRegisterService(LoginView view) {
        this.view = view;
    }


    public void login(LoginRequestMessage event, ChannelHandlerContext ctx) {

        if (event.getUserId()==null || event.getPassword().isEmpty()) {
            view.showMessage("用户名或密码不能为空");
        } else {
            LoginRequestMessage request = new LoginRequestMessage(event.getUserId(), event.getPassword());
            ctx.writeAndFlush(request);
        }
    }

    public void register(RegisterRequestMessage message, ChannelHandlerContext ctx) {

        // 实际注册逻辑...
    }
}
