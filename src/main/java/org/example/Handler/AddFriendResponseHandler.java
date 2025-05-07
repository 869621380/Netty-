// 2. 添加好友响应处理器
package org.example.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;

import org.example.Cache.MessageCache;
import org.example.Controller.ChatListController;
import org.example.Model.message.responseMessage.AddFriendResponseMessage;
import org.example.Service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddFriendResponseHandler extends SimpleChannelInboundHandler<AddFriendResponseMessage> {
    private static final Logger log = LoggerFactory.getLogger(AddFriendResponseHandler.class);

    // @Override
    // protected void channelRead0(ChannelHandlerContext ctx, AddFriendResponseMessage msg) throws Exception {
    //     log.debug("收到添加好友响应：{}", msg);
        
    //     // 将响应传递给待处理的添加好友回调
    //     if (MessageCache.getAddFriendCallback() != null) {
    //         MessageCache.getAddFriendCallback().handleResponse(msg);
    //         // 处理完成后清除回调
    //         MessageCache.setAddFriendCallback(null);
    //     }
    // }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AddFriendResponseMessage msg) throws Exception {
        // 处理主动添加好友的回调
        if (MessageCache.getAddFriendCallback() != null) {
            MessageCache.getAddFriendCallback().handleResponse(msg);
            // 处理完成后清除回调
            MessageCache.setAddFriendCallback(null);
            return; // 这是主动添加的响应，已处理完毕
        }

        // 如果没有回调，说明这是被动添加的通知
        if (msg.isSuccess()) {
            // 获取ChatListController实例
            ChatListController controller = MessageCache.getChatListController();
            if (controller != null) {
                // 处理被添加为好友的通知
                Map<String, Object> friendMap = (Map<String, Object>) msg.getNewFriend();
                controller.addNewFriend(friendMap);
            }
        }
    }
}