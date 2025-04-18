package org.example.Controller;

import io.netty.channel.ChannelHandlerContext;
import lombok.Setter;
import org.example.Model.message.requestMessage.LoginAndRegisterRequestMessage;
import org.example.Model.message.requestMessage.LoginRequestMessage;
import org.example.Model.message.requestMessage.RegisterRequestMessage;
import org.example.Service.LoginRegisterService;
import org.example.View.LoginView;


public class LoginController implements LoginView.LoginMessageListener {
    private final LoginView view;

    LoginRegisterService loginRegisterService;
    @Setter
    ChannelHandlerContext ctx;
    public LoginController(LoginView view) {
        ctx=null;
        this.view = view;
        loginRegisterService=new LoginRegisterService(view);
        view.setLoginMessageListener(this);
    }


    public void showMessage(String message) {
        view.showMessage(message);
    }

    public void handleEvent(LoginAndRegisterRequestMessage event, ChannelHandlerContext ctx) {
        if(ctx==null) {
            view.showMessage("请检查网络连接");
            return;
        }
        if(event instanceof LoginRequestMessage loginRequestMessage) {
            if (loginRequestMessage.getUserId()==null || loginRequestMessage.getPassword().isEmpty()) {
                view.showMessage("用户名或密码不能为空");
            }else loginRegisterService.login(loginRequestMessage,ctx);
        }
        else if(event instanceof RegisterRequestMessage) {

        }

    }

    @Override
    public void onLoginRequest(String username, String password) {
        Integer userId=Integer.valueOf(username);
        LoginRequestMessage event = new LoginRequestMessage(userId, password);
        handleEvent(event,ctx);
    }

    @Override
    public void onRegisterRequest(String username, String password, String email, String code) {
        RegisterRequestMessage event = new RegisterRequestMessage();
        handleEvent(event,ctx);
    }

    @Override
    public void onGetCodeRequest(String email) {
        // 处理获取验证码逻辑
    }

    public void closeLoginView(){
        view.dispose();
    }

}