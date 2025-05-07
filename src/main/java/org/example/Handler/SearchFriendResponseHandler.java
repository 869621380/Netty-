// 1. 搜索好友响应处理器
package org.example.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.Cache.MessageCache;
import org.example.Model.message.responseMessage.SearchFriendResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchFriendResponseHandler extends SimpleChannelInboundHandler<SearchFriendResponseMessage> {
    private static final Logger log = LoggerFactory.getLogger(SearchFriendResponseHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SearchFriendResponseMessage msg) throws Exception {
        log.debug("收到搜索好友响应：{}", msg);
        
        // 将响应传递给待处理的搜索回调
        if (MessageCache.getSearchFriendCallback() != null) {
            MessageCache.getSearchFriendCallback().handleResponse(msg);
            // 处理完成后清除回调
            MessageCache.setSearchFriendCallback(null);
        }
    }
}