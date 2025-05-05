package org.example.View;

import org.example.Model.Domain.FriendInfo;
import org.example.Model.Domain.UserInfo;
import org.example.Service.FriendService;
import org.example.Service.UserInfoService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FriendsPanel extends JPanel {  // 添加继承自JPanel
    private JList<FriendInfo> friendList;
    private DefaultListModel<FriendInfo> listModel;
    private FriendService friendService;

    public FriendsPanel() {
        this.friendService = new FriendService();
        this.listModel = new DefaultListModel<>();

        initComponents();
        loadFriends();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addFriendBtn = new JButton("添加好友");
        addFriendBtn.addActionListener(this::showAddFriendDialog);
        topPanel.add(addFriendBtn);

        friendList = new JList<>(listModel);
        friendList.setCellRenderer(new FriendListCellRenderer());
        friendList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openChatWindow();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(friendList);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void showAddFriendDialog(ActionEvent e) {
        String userId = JOptionPane.showInputDialog(this,
                "请输入好友的用户id:", "添加好友", JOptionPane.QUESTION_MESSAGE);

        if (userId != null && !userId.isEmpty()) {
            boolean sent = friendService.sendFriendRequest(userId);
            if (sent) {
                JOptionPane.showMessageDialog(this,
                        "好友请求已发送", "请求发送成功", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "发送好友请求失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

//    private void openChatWindow() {
//        FriendInfo selected = friendList.getSelectedValue();
//        if (selected != null) {
//            MainFrame.getInstance().openGroupChatWindow(Integer.valueOf(selected.getFriendId()).toString(), selected.getFriendNickName());
//        }
//    }

    private void openChatWindow() {
        FriendInfo selected = friendList.getSelectedValue();
        if (selected != null) {
            // 修改为使用 openChatWithFriend 方法
            MainFrame.getInstance().openChatWithFriend(
                    Integer.parseInt(selected.getFriendId()),
                    selected.getFriendNickName()
            );
        }
    }
    // 添加缺失的loadFriends方法
    public void loadFriends() {
        List<FriendInfo> friends = friendService.getAllFriends();
        listModel.clear();
        if (friends != null) {
            for (FriendInfo friend : friends) {
                listModel.addElement(friend);
            }
        }
    }

    // FriendListCellRenderer作为内部类
    class FriendListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if(value instanceof FriendInfo) {
                FriendInfo friend = (FriendInfo) value;
                String displayText = friend.getFriendNickName();
                if (friend.isOnline()) {
                    displayText += " [在线]";
                } else {
                    displayText += " [离线]";
                }
                label.setText(displayText);
            }
            return label;
        }
    }
}
