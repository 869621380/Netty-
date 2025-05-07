package org.example.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.Cache.MessageCache;
import org.example.Controller.ChatWindowMessageController;
import org.example.Model.Domain.GroupChatMessage;
import org.example.Model.message.requestMessage.SingleChatImageRequestMessage;
import org.example.Model.message.responseMessage.GroupChatImageResponseMessage;
import org.example.Model.message.responseMessage.GroupChatTextResponseMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GroupChatImageResponseHandler extends SimpleChannelInboundHandler<GroupChatImageResponseMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, GroupChatImageResponseMessage groupChatImageResponseMessage) throws Exception {
        //在群聊里，get()传入的键是群聊名。
        System.out.println("消息分发成功");
        ChatWindowMessageController chatWindowMessageController
                = MessageCache.GetChatWindowMessageControllerMap().get(groupChatImageResponseMessage.getGroupName());
        //System.out.println("chatWindowMessageController:"+chatWindowMessageController);
        if(chatWindowMessageController==null)return;
        GroupChatMessage groupChatMessage=
                new GroupChatMessage(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")),
                        GroupChatMessage.SENDING,
                        groupChatImageResponseMessage.getFrom(),groupChatImageResponseMessage.getGroupName(),"text",groupChatImageResponseMessage.getContent());

        groupChatMessage.setType("image");
        groupChatMessage.setContent(((GroupChatImageResponseMessage)groupChatImageResponseMessage).getContent());
        //更新preview
        //MessageCache.getChatListController().updatePreview(groupChatMessage.getSenderID(),"[图片]");
        chatWindowMessageController.receiveMessage(groupChatMessage);
    }
}
