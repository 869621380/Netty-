// package org.example.Handler;

// import io.netty.channel.ChannelHandlerContext;
// import io.netty.channel.SimpleChannelInboundHandler;
// import org.example.Controller.ChatListController;
// import org.example.Controller.ChatWindowMessageController;
// import org.example.Model.Domain.UserInfo;
// import org.example.Model.message.responseMessage.UserInfoResponseMessage;
// import org.example.View.ChatWindow;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// import java.util.List;
// import java.util.Map;
// import java.util.concurrent.ConcurrentHashMap;

// public class UserInfoResponseHandler extends SimpleChannelInboundHandler<UserInfoResponseMessage> {
//     private static final Logger log = LoggerFactory.getLogger(UserInfoResponseHandler.class);

//     // 存储所有活跃的聊天窗口控制器
//     private static final Map<Integer, ChatWindowMessageController> controllers = new ConcurrentHashMap<>();

//     public static void registerController(Integer userId, ChatWindowMessageController controller) {
//         controllers.put(userId, controller);
//     }

//     public static void unregisterController(Integer userId) {
//         controllers.remove(userId);
//     }

//     @Override
//     protected void channelRead0(ChannelHandlerContext ctx, UserInfoResponseMessage msg) throws Exception {
//         log.debug("收到用户信息响应：{}", msg);

//         if (msg.isSuccess() && msg.getUserInfos() != null && !msg.getUserInfos().isEmpty()) {
//             List<UserInfo> userInfos = msg.getUserInfos();

//             // 更新用户信息缓存
//             for (UserInfo userInfo : userInfos) {
//                 // 如果有对应的控制器，通知其更新信息
//                 Integer userId = userInfo.getId();
//                 ChatWindowMessageController controller = controllers.get(userId);
//                 if (controller != null) {
//                     controller.updateUserInfo(userInfo);
//                 }
//             }

//             // 刷新好友列表
//             ChatListController chatListController = ChatListController.getInstance();
//             if (chatListController != null) {
//                 chatListController.refreshFriendList();
//             }
//         } else {
//             log.warn("获取用户信息失败: {}", msg.getReason());
//         }
//     }
// }

package org.example.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.Controller.ChatWindowMessageController;
import org.example.Model.Domain.UserInfo;
import org.example.Model.message.responseMessage.UserInfoResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserInfoResponseHandler extends SimpleChannelInboundHandler<UserInfoResponseMessage> {
    private static final Logger log = LoggerFactory.getLogger(UserInfoResponseHandler.class);
    
    // 保存活跃的聊天窗口控制器
    private static final Map<Integer, ChatWindowMessageController> controllers = new ConcurrentHashMap<>();
    
    // 保存活跃的ChatListController
    private static org.example.Controller.ChatListController chatListController;
    
    // 注册控制器的静态方法
    public static void registerController(Integer userId, ChatWindowMessageController controller) {
        controllers.put(userId, controller);
    }
    
    public static void unregisterController(Integer userId) {
        controllers.remove(userId);
    }
    
    // 注册ChatListController的方法
    public static void registerChatListController(org.example.Controller.ChatListController controller) {
        chatListController = controller;
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UserInfoResponseMessage msg) throws Exception {
        log.debug("收到用户信息响应：{}", msg);
        
        if (msg.isSuccess() && msg.getUserInfos() != null && !msg.getUserInfos().isEmpty()) {
            List<UserInfo> userInfos = msg.getUserInfos();
            
            // 更新用户信息缓存
            for (UserInfo userInfo : userInfos) {
                if (userInfo != null) {
                    // 如果有对应的聊天窗口控制器，通知更新
                    ChatWindowMessageController controller = controllers.get(userInfo.getId());
                    if (controller != null) {
                        controller.updateUserInfo(userInfo);
                    }
                }
            }
            
            // 如果ChatListController已注册，刷新好友列表
            if (chatListController != null) {
                chatListController.refreshFriendList();
            }
        } else {
            log.warn("获取用户信息失败: {}", msg.getReason());
        }
    }
}