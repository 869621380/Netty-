package org.example.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.Cache.MessageCache;
import org.example.Controller.ChatWindowMessageController;
import org.example.Model.Domain.GroupChatMessage;
import org.example.Model.message.responseMessage.GroupChatTextResponseMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class GroupChatTextResponseHandler extends SimpleChannelInboundHandler<GroupChatTextResponseMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, GroupChatTextResponseMessage groupChatTextResponseMessage) throws Exception {
        //在群聊里，get()传入的键是群聊名。
        System.out.println("消息分发成功");
        ChatWindowMessageController chatWindowMessageController
        =MessageCache.GetChatWindowMessageControllerMap().get(groupChatTextResponseMessage.getGroupName());
        //System.out.println("chatWindowMessageController:"+chatWindowMessageController);
        if(chatWindowMessageController==null)return;
        GroupChatMessage groupChatMessage=
                new GroupChatMessage(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")),
                        GroupChatMessage.SENDING,
                        groupChatTextResponseMessage.getFrom(),groupChatTextResponseMessage.getGroupName(),"text",groupChatTextResponseMessage.getContent());

            groupChatMessage.setType("text");
            groupChatMessage.setContent(((GroupChatTextResponseMessage)groupChatTextResponseMessage).getContent());
            //更新preview
            //MessageCache.getChatListController().updatePreview(groupChatMessage.getSenderID(),(String) groupChatMessage.getContent());

        chatWindowMessageController.receiveMessage(groupChatMessage);
    }
}
