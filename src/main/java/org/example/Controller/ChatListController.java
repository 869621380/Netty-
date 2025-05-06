package org.example.Controller;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;
import org.example.Model.Domain.ChatItem;
import org.example.Service.ChatListService;
import org.example.Util.ThreadPoolManager;
import org.example.View.ChatListPanel;
import org.example.View.ChatWindow;
import org.example.View.GroupMemberSelectPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class ChatListController implements ChatListPanel.ChatListListener,
                                        ChatListPanel.CreateGroupListener  {
    private final ChatListPanel view;
    private final ChatListService chatListService;

    @Getter
    private CountDownLatch latch = new CountDownLatch(1);
    @Setter
    ChannelHandlerContext ctx;
    public ChatListController(ChatListPanel view) {
        this.view = view;
        chatListService=new ChatListService();
        view.setChatListener(this);
        view.setCreateGroupListener(this); // 新增这行
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
}
