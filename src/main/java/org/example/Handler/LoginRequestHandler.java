package org.example.Handler;

import org.example.Model.message.LoginMessage;
import org.example.Model.message.LoginRequestMessage;
import org.example.View.LoginView;

import java.util.Objects;

// 登录事件处理器
public class LoginRequestHandler implements LoginMessageHandler {
    private LoginView view;

    public LoginRequestHandler(LoginView view) {
        this.view = view;
    }

    @Override
    public void handle(LoginMessage event) {
        LoginRequestMessage e = (LoginRequestMessage) event;
        System.out.println(e.getUsername());
        System.out.println(e.getPassword());
        if (e.getUsername().isEmpty() || e.getPassword().isEmpty()) {
            view.showMessage("用户名或密码不能为空");
        } else {

            // 实际登录逻辑...
            //if(success)
            //new ChatFrame(username);
        }
    }
}