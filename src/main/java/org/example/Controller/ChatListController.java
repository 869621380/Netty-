package org.example.Controller;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;

import org.example.Cache.MessageCache;
import org.example.Model.Domain.ChatItem;
import org.example.Model.Domain.UserInfo;
import org.example.Model.message.requestMessage.AddFriendRequestMessage;
import org.example.Model.message.requestMessage.SearchFriendRequestMessage;
import org.example.Service.ChatListService;
import org.example.Util.ThreadPoolManager;
import org.example.View.AddFriendDialog;
import org.example.View.ChatListPanel;
import org.example.View.ChatWindow;
import org.example.View.GroupMemberSelectPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ChatListController implements ChatListPanel.ChatListListener,
                                        ChatListPanel.CreateGroupListener,
                                        ChatListPanel.AddFriendListener {

    private final ChatListPanel view;
    private final ChatListService chatListService;


    @Getter
    private CountDownLatch latch = new CountDownLatch(1);
    @Setter
            @Getter
    ChannelHandlerContext ctx;
    public ChatListController(ChatListPanel view) {
        this.view = view;
        chatListService=new ChatListService();
        view.setChatListener(this);
        view.setCreateGroupListener(this);// 新增这行
        view.setAddFriendListener(this);//好友添加
    }

    @Override
    public void setInitData(Integer userId) {
        List<ChatItem>chatItems=chatListService.getChatItems(userId);
        view.addChatItem(chatItems);
        view.revalidate();
        view.repaint();
        ThreadPoolManager.getDBExecutorService().execute(() -> {
            if(chatItems!=null)
                view.addChatWindow(chatItems);

            latch.countDown();
        });

    }

    public void updatePreview(Integer receiverId,String content) {
        view.updateItem(receiverId,content);

        view.revalidate();
        view.repaint();
    }

    public void closeCtx() {
        if(ctx!=null) {
            ctx.close();
        }
    }

    @Override
    public void onAddFriendRequested(Integer userId) {
        Window window = SwingUtilities.getWindowAncestor(view);
        Frame parentFrame = (window instanceof Frame) ? (Frame) window : null;

        AddFriendDialog dialog = new AddFriendDialog(parentFrame, userId);
        dialog.setAddFriendListener(new AddFriendDialog.AddFriendListener() {
            @Override
            public void onSearchFriend(Integer friendId, AddFriendDialog.SearchFriendCallback callback) {
               
                searchUser(friendId, callback);
            }

            @Override
            public void onAddFriend(Integer userId, Integer friendId, AddFriendDialog.AddFriendCallback callback) {
               
                addFriend(userId, friendId, callback);
            }
        });
        dialog.setVisible(true);
    }
    private void searchUser(Integer friendId, AddFriendDialog.SearchFriendCallback callback) {
    // 检查是否已经连接到服务器
    if (ctx == null) {
        SwingUtilities.invokeLater(() -> {
            callback.onFailure("未连接到服务器，请稍后重试");
        });
        return;
    }
    
    // 创建搜索好友请求消息
    SearchFriendRequestMessage requestMessage = new SearchFriendRequestMessage(view.getUserId(), friendId);
    
    // 设置回调处理响应
    MessageCache.setSearchFriendCallback(response -> {
        if (response.isSuccess()) {
            // 搜索成功，返回用户信息
            UserInfo userInfo = response.getUserInfo();
            
            // 创建用户头像
            ImageIcon avatar = null;
            if (userInfo.getAvatarPath() != null && !userInfo.getAvatarPath().isEmpty()) {
                try {
                    // 假设是从本地资源加载头像
                    avatar = new ImageIcon(getClass().getResource("/avatars/" + userInfo.getAvatarPath()));
                    // 缩放头像大小
                    if (avatar.getIconWidth() != -1) {
                        Image img = avatar.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                        avatar = new ImageIcon(img);
                    }
                } catch (Exception e) {
                    // 如果加载失败，使用默认头像
                    avatar = createDefaultAvatar(friendId, 40, 40);
                }
            } else {
                avatar = createDefaultAvatar(friendId, 40, 40);
            }
            
            final ImageIcon finalAvatar = avatar;
            SwingUtilities.invokeLater(() -> {
                if (response.isAlreadyFriend()) {
                    callback.onFailure("该用户已经是您的好友");
                } else {
                    callback.onSuccess(userInfo.getNickname(), finalAvatar);
                }
            });
        } else {
            // 搜索失败
            SwingUtilities.invokeLater(() -> {
                callback.onFailure(response.getReason());
            });
        }
    });
    
    // 发送请求
    ctx.writeAndFlush(requestMessage);
}

private void addFriend(Integer userId, Integer friendId, AddFriendDialog.AddFriendCallback callback) {
    // 检查是否已经连接到服务器
    if (ctx == null) {
        SwingUtilities.invokeLater(() -> {
            callback.onFailure("未连接到服务器，请稍后重试");
        });
        return;
    }
    
    // 创建添加好友请求消息
    AddFriendRequestMessage requestMessage = new AddFriendRequestMessage(userId, friendId);
    
    // 设置回调处理响应
    MessageCache.setAddFriendCallback(response -> {
        if (response.isSuccess()) {
            // 添加好友成功，获取新好友信息

            Map<String,Object> newFriends = (Map<String, Object>) response.getNewFriend();

            Double receiverIdt= (Double) newFriends.get("receiverId");

            Integer receiverId = receiverIdt.intValue();
            String receiverNickname= newFriends.get("name").toString();
            String avatarPath="img.png";
            Integer unreadCount=1;
            String preview="已成功添加好友";
            ChatItem newFriend = new ChatItem(0,(Integer) receiverId,receiverNickname,avatarPath,unreadCount,preview,null);
            System.out.println(newFriend);
            SwingUtilities.invokeLater(() -> {
                // 更新UI
                view.addChatItem(Collections.singletonList(newFriend));
               // view.addChatWindow(Collections.singletonList(newFriend));
                view.addChatWindow(newFriend,"添加好友");
                // 通知回调添加成功
                callback.onSuccess();
                
                // 如果需要，打开与新好友的聊天窗口
                ChatWindow friendWindow = view.getChatWindowMap().get(receiverId);
                if (view.getCurrentChatWindow() != null) {
                    view.getCurrentChatWindow().setVisible(false);
                }
                view.setCurrentChatWindow(friendWindow);
                friendWindow.setVisible(true);
            });
        } else {
            // 添加失败
            SwingUtilities.invokeLater(() -> {
                callback.onFailure(response.getReason());
            });
        }
    });
    
    // 发送请求
    ctx.writeAndFlush(requestMessage);
}

// 辅助方法：创建默认头像
    private ImageIcon createDefaultAvatar(int userId, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
    
    // 根据用户ID生成颜色
        int r = (userId * 123) % 256;
        int g = (userId * 255) % 256;
        int b = (userId * 189) % 256;
        g2d.setColor(new Color(r, g, b));
        g2d.fillRect(0, 0, width, height);
    
    // 添加用户ID的第一个字符作为标识
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        String label = String.valueOf(userId).substring(0, 1);
        FontMetrics fm = g2d.getFontMetrics();
        int strWidth = fm.stringWidth(label);
        int strHeight = fm.getHeight();
        g2d.drawString(label, (width - strWidth) / 2, height / 2 + strHeight / 4);
    
        g2d.dispose();
        return new ImageIcon(img);
    }

    @Override
    public void onCreateGroupRequested(Integer userId) {
        // 获取当前所有联系人作为可选成员
        List<ChatItem> allContacts = chatListService.getChatItems(userId);

        // 创建群成员选择对话框
        GroupMemberSelectPanel selectPanel = new GroupMemberSelectPanel(allContacts);
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(view), "创建新群聊", true);
        dialog.setContentPane(selectPanel);
        dialog.setSize(400, 600);
        dialog.setLocationRelativeTo(view);

        // 确认按钮事件
        selectPanel.getConfirmButton().addActionListener(e -> {
            List<ChatItem> selectedMembers = selectPanel.getSelectedMembers();
            if (selectedMembers.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "请至少选择一名成员", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 实际创建群聊的逻辑
            createNewGroup(selectedMembers,userId,dialog);
            dialog.dispose();
        });

        dialog.setVisible(true);
    }
    private void createNewGroup(List<ChatItem> selectedMembers,Integer userId,JDialog dialog) {
        // 这里实现实际的群聊创建逻辑
        // 1. 弹出输入群名称的对话框
        String groupName = JOptionPane.showInputDialog(view, "请输入群名称:", "群名称", JOptionPane.PLAIN_MESSAGE);
        if (groupName == null || groupName.trim().isEmpty()) {
            return;
        }

        // 2. 调用服务层创建群聊
//        List<Integer> memberIds = selectedMembers.stream()
//                .map(ChatItem::getReceiverId)
//                .collect(Collectors.toList());

        // 假设有一个服务方法 createGroup
//        ChatItem newGroup = chatListService.createGroup(userId, groupName, memberIds);

        //测试代码
        ChatItem newGroup=new ChatItem(1,2,groupName,"img.png",1,"这是个群聊",null);

        // 3. 更新UI
        view.addChatItem(Collections.singletonList(newGroup));
        view.addChatWindow(Collections.singletonList(newGroup));

        // 4. 打开新群聊窗口
        ChatWindow groupWindow = view.getChatWindowMap().get(newGroup.getReceiverId());
        if (view.getCurrentChatWindow() != null) {
            view.getCurrentChatWindow().setVisible(false);
        }
        view.setCurrentChatWindow(groupWindow);
        groupWindow.setVisible(true);

    }

    public void sendInitMessage(Integer userId) {
        if(ctx!=null) {
            chatListService.sendInitMessage(userId, ctx);
        }else{
            System.out.println("ctx为空");
        }
    }


    // 添加获取当前用户ID的方法
    public Integer getCurrentUserId() {
        return view.getUserId();
    }

    // 添加刷新好友列表的方法
    public void refreshFriendList() {
        Integer userId = getCurrentUserId();
        if (userId != null) {
            List<ChatItem> chatItems = chatListService.getChatItems(userId);
        
            SwingUtilities.invokeLater(() -> {
                // 清空现有列表
                view.clearChatList();
            
                // 添加刷新后的列表
                view.addChatItem(chatItems);
            
                view.revalidate();
                view.repaint();
            });
        }
    }

    // 在ChatListController类中添加此方法
    public void addNewFriend(Map<String, Object> friendMap) {
        try {
            // 从Map中提取好友信息
            Double receiverIdDouble = (Double) friendMap.get("receiverId");
            Integer receiverId = receiverIdDouble.intValue();
            String receiverName = (String) friendMap.get("name");
            String avatarPath = "default.png"; // 默认头像
            String preview = (String) friendMap.get("lastMsg");
            String lastMsgTime = (String) friendMap.get("lastMsgTime");
            Integer unreadCount = ((Number) friendMap.get("unreadCount")).intValue();

            // 创建新的聊天项
            ChatItem newFriend = new ChatItem(
                    0, // 单聊类型
                    receiverId,
                    receiverName,
                    avatarPath,
                    unreadCount,
                    preview,
                    lastMsgTime
            );

            // 在EDT线程中更新UI
            SwingUtilities.invokeLater(() -> {
                // 添加到聊天列表
                view.addChatItem(Collections.singletonList(newFriend));

                // 创建聊天窗口
                //view.addChatWindow(newFriend);
                view.addChatWindow(newFriend,"newFriend");
                // 刷新UI
                view.revalidate();
                view.repaint();

                // 显示通知
                JOptionPane.showMessageDialog(null, "用户 " + receiverName + " 已添加您为好友");
            });
        } catch (Exception e) {
            System.err.println("处理新好友信息失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
