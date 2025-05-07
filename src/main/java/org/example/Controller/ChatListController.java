package org.example.Controller;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;
import org.example.Model.Domain.ChatItem;
import org.example.Model.message.responseMessage.GroupCreateResponseMessage;
import org.example.Service.ChatListService;
import org.example.Service.GroupManageService;
import org.example.Util.ThreadPoolManager;
import org.example.View.ChatListPanel;
import org.example.View.ChatWindow;
import org.example.View.GroupMemberSelectPanel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class ChatListController implements ChatListPanel.ChatListListener,
                                        ChatListPanel.CreateGroupListener  {
    private final ChatListPanel view;
    private final ChatListService chatListService;
    private final GroupManageService groupManageService;
    @Getter
    private CountDownLatch latch = new CountDownLatch(1);
    @Setter
    ChannelHandlerContext ctx;
    public ChatListController(ChatListPanel view) {
        this.view = view;
        chatListService=new ChatListService();
        groupManageService=new GroupManageService();
        view.setChatListener(this);
        view.setCreateGroupListener(this); // 新增这行
    }

    @Override
    public void setInitData(Integer userId) {
        //单聊+群聊列表
        List<ChatItem>chatItems=chatListService.getChatItems(userId);
        view.addChatItem(chatItems);
        view.revalidate();
        view.repaint();
        ThreadPoolManager.getDBExecutorService().execute(() -> {
            if(chatItems!=null)
                view.addChatWindow(chatItems); //修改聊天框的记录获取方式，兼容单聊和群聊

            latch.countDown();
        });

    }
//    public void updateChatWindow(String groupName){
//        System.out.println(view.getChatWindowMap().get(groupName));
//        view.getChatWindowMap().get(groupName).setVisible(true);
//    }
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
        List<ChatItem> singChatItems=new ArrayList<>();
        for (int i = 0; i < allContacts.size(); i++) {
            if(allContacts.get(i).getReceiverType()==0)
                singChatItems.add(allContacts.get(i));
        }

        // 创建群成员选择对话框
        GroupMemberSelectPanel selectPanel = new GroupMemberSelectPanel(singChatItems);
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(view), "创建新群聊", true);
        dialog.setContentPane(selectPanel);
        dialog.setSize(400, 600);
        dialog.setLocationRelativeTo(view);

        // 确认按钮事件
        selectPanel.getConfirmButton().addActionListener(e -> {
            Set<Integer> selectedMembers = selectPanel.getSelectedMembers();
            if (selectedMembers.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "请至少选择一名成员", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 实际创建群聊的逻辑
            selectedMembers.add(userId);//加上自己
            createNewGroup(userId,selectedMembers,ctx);
            dialog.dispose();
        });

        dialog.setVisible(true);
    }
    private void createNewGroup(Integer userId,Set<Integer> selectedMembers,ChannelHandlerContext ctx) {
        // 这里实现实际的群聊创建逻辑
        // 1. 弹出输入群名称的对话框
        String groupName = JOptionPane.showInputDialog(view, "请输入群名称:", "群名称", JOptionPane.PLAIN_MESSAGE);
        if (groupName == null || groupName.trim().isEmpty()) {
            return;
        }

        // 2. 调用服务层创建群聊
        groupManageService.createGroup(userId,groupName,selectedMembers,ctx);

        return;
    }

    public void addGroupItem(GroupCreateResponseMessage groupCreateResponseMessage){

        ChatItem newGroup=new ChatItem(1,-1,groupCreateResponseMessage.getReason().split(":")[1],"img.png",1,groupCreateResponseMessage.getReason(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        // 3. 更新UI
        view.addChatItem(Collections.singletonList(newGroup));
        view.addChatWindow(Collections.singletonList(newGroup));

        // 4. 打开新群聊窗口
        ChatWindow groupWindow = view.getChatWindowMap().get(newGroup.getReceiverName());
        //System.out.println("新窗口："+newGroup.getReceiverName());
        //System.out.println("view:"+view);
        if (view.getCurrentChatWindow() != null) {
            view.getCurrentChatWindow().setVisible(false);
        }
        view.setCurrentChatWindow(groupWindow);
        groupWindow.setVisible(true);

    }
    public void addGroupItem(GroupCreateResponseMessage groupCreateResponseMessage,ChannelHandlerContext ctx){

        ChatItem newGroup=new ChatItem(1,-1,groupCreateResponseMessage.getReason().split(":")[1],"img.png",1,groupCreateResponseMessage.getReason(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        // 3. 更新UI
        view.addChatItem(Collections.singletonList(newGroup));
        view.addChatWindow(Collections.singletonList(newGroup),ctx);

        // 4. 打开新群聊窗口
        ChatWindow groupWindow = view.getChatWindowMap().get(newGroup.getReceiverName());
        //System.out.println("新窗口："+newGroup.getReceiverName());
        //System.out.println("view:"+view);
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
