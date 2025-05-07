package org.example.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.Cache.MessageCache;
import org.example.Model.message.responseMessage.GroupCreateResponseMessage;
import org.example.Service.GroupManageService;
import org.example.View.ChatListPanel;

public class GroupCreateResponseHandler extends SimpleChannelInboundHandler<GroupCreateResponseMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateResponseMessage groupCreateResponseMessage) throws Exception {
        if (groupCreateResponseMessage.isSuccess()) {
            //更新聊天列表
            MessageCache.getChatListController().addGroupItem(groupCreateResponseMessage,ctx);
            //在群聊

        } else {
            System.out.println("建群失败");
        }
    }
}
