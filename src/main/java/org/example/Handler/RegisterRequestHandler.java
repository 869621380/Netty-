package org.example.Handler;

import org.example.Model.message.LoginMessage;
import org.example.Model.message.RegisterRequestMessage;
import org.example.View.LoginView;

public class RegisterRequestHandler implements LoginMessageHandler {
    private LoginView view;

    public RegisterRequestHandler(LoginView view) {
        this.view = view;
    }

    @Override
    public void handle(LoginMessage message) {
        RegisterRequestMessage e = (RegisterRequestMessage) message;
        // 实际注册逻辑...
    }
}