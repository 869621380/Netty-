package org.example.Controller;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.Model.message.requestMessage.LoginAndRegisterRequestMessage;
import org.example.Model.message.requestMessage.LoginRequestMessage;
import org.example.Model.message.requestMessage.RegisterCodeRequestMessage;
import org.example.Model.message.requestMessage.RegisterRequestMessage;
import org.example.Service.LoginRegisterService;
import org.example.View.LoginView;

import java.util.regex.Pattern;


@Slf4j
public class LoginController implements LoginView.LoginMessageListener {

    final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private final LoginView view;
    @Getter
    @Setter
    private static String token=null;
    LoginRegisterService loginRegisterService;
    @Setter
    ChannelHandlerContext ctx;
    public LoginController(LoginView view) {
        ctx=null;
        token=null;
        this.view = view;
        loginRegisterService=new LoginRegisterService();
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
        else if(event instanceof RegisterRequestMessage registerRequestMessage) {

            if(registerRequestMessage.getPassword()==null||registerRequestMessage.getPassword().isEmpty())
                view.showMessage("密码不能为空");
            else {
                loginRegisterService.register(registerRequestMessage,ctx);
            }

        }else if (event instanceof RegisterCodeRequestMessage registerCodeRequestMessage) {

            if (registerCodeRequestMessage.getEmail().isEmpty()) {
                view.showMessage("邮箱不能为空");
            } else if (!EMAIL_PATTERN.matcher(registerCodeRequestMessage.getEmail()).matches()) {
                view.showMessage("请输入正确的邮箱格式");
            } else {
                loginRegisterService.getCode(registerCodeRequestMessage, ctx);
                view.showMessage("验证码已发送，请注意查收");
                view.disableGetCodeButtonFor30Seconds();
            }
        }

    }

    @Override
    public void onLoginRequest(String username, String password) {
        Integer userId=Integer.valueOf(username);
        LoginRequestMessage event = new LoginRequestMessage(userId, password);
        handleEvent(event,ctx);
    }

    @Override
    public void onRegisterRequest(String email, String code,String password) {
        RegisterRequestMessage event = new RegisterRequestMessage(email, code, password);
        handleEvent(event,ctx);
    }

    @Override
    public void onGetCodeRequest(String email) {
        RegisterCodeRequestMessage event = new RegisterCodeRequestMessage(email);
        handleEvent(event,ctx);
    }

    public void closeLoginView(){
        view.dispose();
    }




}