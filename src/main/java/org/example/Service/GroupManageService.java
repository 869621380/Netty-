package org.example.Service;

import io.netty.channel.ChannelHandlerContext;
import org.example.Model.Domain.ChatItem;
import org.example.Model.message.requestMessage.GroupCreateRequestMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

public class GroupManageService {

    public ChatItem createGroup(Integer creatorId, String groupName, Set<Integer> memberIds, ChannelHandlerContext ctx) {
        // 实现创建群聊的逻辑
        GroupCreateRequestMessage groupCreateRequestMessage=new GroupCreateRequestMessage(creatorId,groupName,memberIds);
        if(ctx!=null&&groupCreateRequestMessage!=null){
            ctx.writeAndFlush(groupCreateRequestMessage);
        }
        // 返回新创建的群聊项
        ChatItem group = new ChatItem();
        group.setReceiverId(1); // 假设这是群ID
        group.setReceiverName(groupName);
        group.setPreview("您已创建群聊");
        group.setPreviewTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        group.setUnreadCount(0);
        group.setAvatarPath("img.png");

        return group;
    }
}
