package org.example.Controller;

import org.example.Handler.LoginMessageHandler;
import org.example.Handler.LoginRequestHandler;
import org.example.Handler.RegisterRequestHandler;
import org.example.Model.message.LoginMessage;
import org.example.Model.message.LoginRequestMessage;
import org.example.Model.message.RegisterRequestMessage;
import org.example.View.LoginView;

import java.util.HashMap;
import java.util.Map;


public class LoginController implements LoginView.LoginMessageListener {
    private final LoginView view;
    private final Map<Class<? extends LoginMessage>, LoginMessageHandler> handlers;

    public LoginController(LoginView view) {
        this.view = view;
        this.handlers = new HashMap<>();
        registerHandlers();
        view.setLoginMessageListener(this);
    }

    private void registerHandlers() {
        handlers.put(LoginRequestMessage.class, new LoginRequestHandler(view));
        handlers.put(RegisterRequestMessage.class, new RegisterRequestHandler(view));
    }

    public void handleEvent(LoginMessage event) {
        LoginMessageHandler handler = handlers.get(event.getClass());
        if (handler != null) {
            handler.handle(event);
        } else {
            throw new UnsupportedOperationException("未支持的事件类型: " + event.getClass());
        }

    }

    @Override
    public void onLoginRequest(String username, String password) {
        LoginRequestMessage event = new LoginRequestMessage(username, password);
        handleEvent(event);
    }

    @Override
    public void onRegisterRequest(String username, String password, String email, String code) {
        RegisterRequestMessage event = new RegisterRequestMessage();
        handleEvent(event);
    }

    @Override
    public void onGetCodeRequest(String email) {
        // 处理获取验证码逻辑
    }


}