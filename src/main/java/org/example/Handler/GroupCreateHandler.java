package org.example.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.Model.Domain.GroupInfo;
import org.example.Model.message.responseMessage.GroupCreateResponseMessage;
import org.example.Service.GroupService;
import org.example.View.MainFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class GroupCreateHandler extends SimpleChannelInboundHandler<GroupCreateResponseMessage> {
    private static final Logger logger = LoggerFactory.getLogger(GroupCreateHandler.class);

//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateResponseMessage msg) throws Exception {
//        logger.info("收到创建群组响应: {}", msg);
//
//        if (msg.isSuccess()) {
//            GroupInfo groupInfo = msg.getGroupInfo();
//            boolean saved = new GroupService().saveGroup(groupInfo);
//
//            if (saved) {
//                // 通知UI更新群组列表
//                SwingUtilities.invokeLater(() -> {
//                    MainFrame.getInstance().refreshGroupList();
//                    JOptionPane.showMessageDialog(MainFrame.getInstance(),
//                            "群组创建成功: " + groupInfo.getGroupName(),
//                            "创建成功", JOptionPane.INFORMATION_MESSAGE);
//                });
//            }
//        } else {
//            SwingUtilities.invokeLater(() -> {
//                JOptionPane.showMessageDialog(MainFrame.getInstance(),
//                        "创建群组失败: " + msg.getReason(),
//                        "创建失败", JOptionPane.ERROR_MESSAGE);
//            });
//        }
//    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateResponseMessage msg) throws Exception {
        logger.info("收到创建群组响应: {}", msg);

        if (msg.isSuccess()) {
            // 从 msg.getGroupInfo() 获取群组信息
            GroupInfo groupInfo = msg.getGroupInfo(); // 注意这里使用的是 groupInfo 而不是 groupinfo

            if (groupInfo != null) {
                boolean saved = new GroupService().saveGroup(groupInfo);

                if (saved) {
                    // 通知UI更新群组列表
                    SwingUtilities.invokeLater(() -> {
                        MainFrame.getInstance().refreshGroupList();
                        JOptionPane.showMessageDialog(MainFrame.getInstance(),
                                "群组创建成功: " + groupInfo.getGroupName(),
                                "创建成功", JOptionPane.INFORMATION_MESSAGE);
                    });
                } else {
                    logger.error("保存群组信息到本地数据库失败");
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(MainFrame.getInstance(),
                                "群组创建成功，但保存到本地数据库失败",
                                "部分成功", JOptionPane.WARNING_MESSAGE);
                    });
                }
            } else {
                logger.error("服务器返回的群组信息为空");
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(MainFrame.getInstance(),
                            "服务器返回的群组信息为空",
                            "创建失败", JOptionPane.ERROR_MESSAGE);
                });
            }
        } else {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(MainFrame.getInstance(),
                        "创建群组失败: " + msg.getReason(),
                        "创建失败", JOptionPane.ERROR_MESSAGE);
            });
        }
    }
}