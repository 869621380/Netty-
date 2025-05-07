package org.example.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.Cache.MessageCache;
import org.example.Model.message.responseMessage.GroupCreateResponseMessage;
import org.example.Service.ChatListService;
import org.example.Service.GroupManageService;
import org.example.View.ChatListPanel;

public class GroupCreateResponseHandler extends SimpleChannelInboundHandler<GroupCreateResponseMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateResponseMessage groupCreateResponseMessage) throws Exception {
        if (groupCreateResponseMessage.isSuccess()) {
            //更新聊天列表
            if(!groupCreateResponseMessage.getReason().split(":")[0].equals("成功创建群聊")) {
                MessageCache.getChatListController().addGroupItem(groupCreateResponseMessage, ctx);
            }
            //更新本地数据库
            ChatListService chatListService=new ChatListService();
            chatListService.createGroup(groupCreateResponseMessage.getCreaterId(),groupCreateResponseMessage.getReason().split(":")[1],groupCreateResponseMessage.getGroupMember());

        } else {
            System.out.println("建群失败");
        }
    }
}
