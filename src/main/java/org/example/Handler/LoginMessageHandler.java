package org.example.Handler;

import org.example.Model.message.LoginMessage;

public interface LoginMessageHandler {
    void handle(LoginMessage message);
}