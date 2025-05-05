//package org.example.Handler;
//
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//import org.example.Client;
//import org.example.Model.Domain.FriendInfo;
//import org.example.Model.message.responseMessage.FriendRequestResponseMessage;
//import org.example.Service.FriendService;
//import org.example.View.MainFrame;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.swing.*;
//import java.util.Map;
//
//public class FriendRequestHandler extends SimpleChannelInboundHandler<FriendRequestResponseMessage> {
//    private static final Logger logger = LoggerFactory.getLogger(FriendRequestHandler.class);
//
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, FriendRequestResponseMessage msg) throws Exception {
//        logger.info("收到好友请求响应: {}", msg);
//
//        if (msg.isSuccess()) {
//            // 从响应中获取好友信息Map
//            Map<String, Object> friendInfoMap = msg.getFriendInfo();
//            if (friendInfoMap != null) {
//                logger.debug("接收到的好友信息: {}", friendInfoMap);
//
//                // 创建新的FriendInfo对象
//                FriendInfo friendInfo = new FriendInfo();
//
//                // 获取当前用户ID并转换为String类型
//                // 注意：字段名是 userid (小写的i)，不是 userId
//                friendInfo.setUserid(Client.getInstance().getCurrentUser().getId().toString());
//
//                // 处理好友ID
//                if (friendInfoMap.containsKey("friendId")) {
//                    friendInfo.setFriendId(String.valueOf(friendInfoMap.get("friendId")));
//                }
//
//                // 处理昵称字段
//                if (friendInfoMap.containsKey("friendNickname")) {
//                    friendInfo.setFriendNickName(String.valueOf(friendInfoMap.get("friendNickname")));
//                }
//
//                // 处理头像字段
//                if (friendInfoMap.containsKey("friendAvatar")) {
//                    friendInfo.setFriendAvatar(String.valueOf(friendInfoMap.get("friendAvatar")));
//                }
//
//                // 设置在线状态为离线（默认）
//                friendInfo.setOnline(false);
//
//                // 保存到本地数据库
//                boolean added = new FriendService().addFriend(friendInfo);
//
//                if (added) {
//                    // 通知UI更新好友列表
//                    SwingUtilities.invokeLater(() -> {
//                        MainFrame.getInstance().refreshFriendList();
//                        JOptionPane.showMessageDialog(MainFrame.getInstance(),
//                                "已成功添加好友: " + friendInfo.getFriendNickName(),
//                                "添加成功", JOptionPane.INFORMATION_MESSAGE);
//                    });
//                } else {
//                    logger.error("添加好友到本地数据库失败");
//                    SwingUtilities.invokeLater(() -> {
//                        JOptionPane.showMessageDialog(MainFrame.getInstance(),
//                                "添加好友失败: 保存到本地数据库出错",
//                                "添加失败", JOptionPane.ERROR_MESSAGE);
//                    });
//                }
//            } else {
//                logger.error("好友信息为空");
//                SwingUtilities.invokeLater(() -> {
//                    JOptionPane.showMessageDialog(MainFrame.getInstance(),
//                            "添加好友失败: 服务器返回的好友信息为空",
//                            "添加失败", JOptionPane.ERROR_MESSAGE);
//                });
//            }
//        } else {
//            SwingUtilities.invokeLater(() -> {
//                JOptionPane.showMessageDialog(MainFrame.getInstance(),
//                        "添加好友失败: " + msg.getReason(),
//                        "添加失败", JOptionPane.ERROR_MESSAGE);
//            });
//        }
//    }
//}

package org.example.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.Client;
import org.example.Model.Domain.FriendInfo;
import org.example.Model.message.responseMessage.FriendRequestResponseMessage;
import org.example.Service.FriendService;
import org.example.View.MainFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.Map;

public class FriendRequestHandler extends SimpleChannelInboundHandler<FriendRequestResponseMessage> {
    private static final Logger logger = LoggerFactory.getLogger(FriendRequestHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FriendRequestResponseMessage msg) throws Exception {
        logger.info("收到好友请求响应: {}", msg);

        if (msg.isSuccess()) {
            // 从响应中获取好友信息Map
            Map<String, Object> friendInfoMap = msg.getFriendInfo();
            if (friendInfoMap != null) {
                logger.debug("接收到的好友信息: {}", friendInfoMap);

                // 创建新的FriendInfo对象
                FriendInfo friendInfo = new FriendInfo();

                // 获取当前用户ID并转换为String类型
                friendInfo.setUserid(Client.getInstance().getCurrentUser().getId().toString());

                // 处理好友ID
                if (friendInfoMap.containsKey("friendId")) {
                    friendInfo.setFriendId(String.valueOf(friendInfoMap.get("friendId")));
                }

                // 处理昵称字段
                if (friendInfoMap.containsKey("friendNickname")) {
                    friendInfo.setFriendNickName(String.valueOf(friendInfoMap.get("friendNickname")));
                }

                // 处理头像字段
                if (friendInfoMap.containsKey("friendAvatar")) {
                    friendInfo.setFriendAvatar(String.valueOf(friendInfoMap.get("friendAvatar")));
                }

                // 设置在线状态为离线（默认）
                friendInfo.setOnline(false);

                // 保存到本地数据库
                boolean added = new FriendService().addFriend(friendInfo);

                if (added) {
                    // 通知UI更新好友列表
                    SwingUtilities.invokeLater(() -> {
                        MainFrame.getInstance().refreshFriendList();
                        JOptionPane.showMessageDialog(MainFrame.getInstance(),
                                "已成功添加好友: " + friendInfo.getFriendNickName(),
                                "添加成功", JOptionPane.INFORMATION_MESSAGE);
                    });
                } else {
                    logger.error("添加好友到本地数据库失败");
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(MainFrame.getInstance(),
                                "添加好友失败: 保存到本地数据库出错",
                                "添加失败", JOptionPane.ERROR_MESSAGE);
                    });
                }
            } else {
                logger.error("好友信息为空");
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(MainFrame.getInstance(),
                            "添加好友失败: 服务器返回的好友信息为空",
                            "添加失败", JOptionPane.ERROR_MESSAGE);
                });
            }
        } else {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(MainFrame.getInstance(),
                        "添加好友失败: " + msg.getReason(),
                        "添加失败", JOptionPane.ERROR_MESSAGE);
            });
        }
    }
}