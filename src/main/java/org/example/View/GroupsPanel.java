package org.example.View;

import org.example.Client;
import org.example.Model.Domain.FriendInfo;
import org.example.Model.Domain.GroupInfo;
import org.example.Service.FriendService;
import org.example.Service.GroupService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GroupsPanel extends JPanel {
    private JList<GroupInfo> groupList;
    private DefaultListModel<GroupInfo> listModel;
    private GroupService groupService;
    private FriendService friendService;

    public GroupsPanel() {
        this.groupService = new GroupService();
        this.friendService = new FriendService();
        this.listModel = new DefaultListModel<>();

        initComponents();
        loadGroups();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton createGroupBtn = new JButton("创建群组");
        createGroupBtn.addActionListener(this::showCreateGroupDialog);
        topPanel.add(createGroupBtn);

        groupList = new JList<>(listModel);
        groupList.setCellRenderer(new GroupListCellRenderer());
        groupList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openGroupChatWindow();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(groupList);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void loadGroups() {
        List<GroupInfo> groups = groupService.getUserGroups();

        listModel.clear();
        if (groups != null) {
            for (GroupInfo group : groups) {
                listModel.addElement(group);
            }
        }
    }

//    private void showCreateGroupDialog(ActionEvent e) {
//        JTextField groupNameField = new JTextField(20);
//
//        JPanel panel = new JPanel(new GridLayout(0, 1));
//        panel.add(new JLabel("群组名称:"));
//        panel.add(groupNameField);
//
//        int result = JOptionPane.showConfirmDialog(this, panel,
//                "创建群组", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
//
//        if (result == JOptionPane.OK_OPTION) {
//            String groupName = groupNameField.getText().trim();
//
//            if (!groupName.isEmpty()) {
//                // 选择好友添加到群组
//                List<FriendInfo> friends = friendService.getAllFriends();
//                JList<FriendInfo> friendJList = new JList<>(friends.toArray(new FriendInfo[0]));
//                friendJList.setCellRenderer(new FriendSelectionRenderer());
//
//                JScrollPane scrollPane = new JScrollPane(friendJList);
//                scrollPane.setPreferredSize(new Dimension(300, 200));
//
//                int selectResult = JOptionPane.showConfirmDialog(this,
//                        scrollPane, "选择群成员", JOptionPane.OK_CANCEL_OPTION);
//
//                if (selectResult == JOptionPane.OK_OPTION) {
//                    List<String> memberIds = new ArrayList<>();
//                    for (FriendInfo friend : friendJList.getSelectedValuesList()) {
//                        memberIds.add(friend.getFriendId());
//                    }
//
//                    boolean created = groupService.createGroup(groupName, "", memberIds);
//                    if (created) {
//                        JOptionPane.showMessageDialog(this,
//                                "群组创建请求已发送", "请求发送成功", JOptionPane.INFORMATION_MESSAGE);
//                    } else {
//                        JOptionPane.showMessageDialog(this,
//                                "创建群组失败", "错误", JOptionPane.ERROR_MESSAGE);
//                    }
//                }
//            }
//        }
//    }

    private void showCreateGroupDialog(ActionEvent e) {
        JTextField groupNameField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("群组名称:"));
        panel.add(groupNameField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "创建群组", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String groupName = groupNameField.getText().trim();

            if (!groupName.isEmpty()) {
                // 选择好友添加到群组
                List<FriendInfo> friends = friendService.getAllFriends();

                if (friends == null || friends.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "您还没有好友，无法创建群组",
                            "创建失败", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JList<FriendInfo> friendJList = new JList<>(friends.toArray(new FriendInfo[0]));
                friendJList.setCellRenderer(new FriendSelectionRenderer());
                friendJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

                JScrollPane scrollPane = new JScrollPane(friendJList);
                scrollPane.setPreferredSize(new Dimension(300, 200));

                int selectResult = JOptionPane.showConfirmDialog(this,
                        scrollPane, "选择群成员", JOptionPane.OK_CANCEL_OPTION);

                if (selectResult == JOptionPane.OK_OPTION) {
                    List<FriendInfo> selectedFriends = friendJList.getSelectedValuesList();
                    List<String> memberIds = new ArrayList<>();

                    // 添加当前用户
                    memberIds.add(Client.getInstance().getCurrentUser().getId().toString());

                    // 添加选中的好友
                    for (FriendInfo friend : selectedFriends) {
                        memberIds.add(friend.getFriendId());
                    }

                    // 调用创建群组服务
                    boolean success = groupService.createGroup(groupName, "default_avatar.png", memberIds);

                    if (success) {
                        JOptionPane.showMessageDialog(this,
                                "群组创建请求已发送", "请求发送成功",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "群组创建请求发送失败", "请求失败",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "群组名称不能为空", "创建失败",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openGroupChatWindow() {
        GroupInfo selected = groupList.getSelectedValue();
        if (selected != null) {
            MainFrame.getInstance().openGroupChatWindow(selected.getGroupId(), selected.getGroupName());
        }
    }

    // 自定义群组列表单元渲染器
    class GroupListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);

            if (value instanceof GroupInfo) {
                GroupInfo group = (GroupInfo) value;
                label.setText(group.getGroupName());
            }

            return label;
        }
    }

    // 添加到GroupsPanel类的底部
// 自定义好友选择渲染器
    class FriendSelectionRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);

            if (value instanceof FriendInfo) {
                FriendInfo friend = (FriendInfo) value;
                label.setText(friend.getFriendNickName());
                // 如果需要也可以设置图标
                // label.setIcon(...);
            }

            return label;
        }
    }
}
