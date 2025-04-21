package org.example.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.Cache.MessageCache;
import org.example.Controller.ChatWindowMessageController;
import org.example.Model.message.responseMessage.LoginStatusResponseMessage;

public class LoginStatusResponseHandler extends SimpleChannelInboundHandler<LoginStatusResponseMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginStatusResponseMessage loginStatusResponseMessage) throws Exception {
        Integer receiverId=loginStatusResponseMessage.getReceiverId();
        //获取页面控制器
        ChatWindowMessageController chatWindowMessageController
                = MessageCache.GetChatWindowMessageControllerMap().get(receiverId);
        if (chatWindowMessageController != null) {
            chatWindowMessageController.setLoginStatus(loginStatusResponseMessage.getLoginStatus());
        }
    }
}
