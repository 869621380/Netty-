package org.example.View;

import org.example.Model.Domain.ChatItem;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static org.example.Util.Constants.DEFAULT_AVATAR;

public class GroupMemberSelectPanel extends JPanel {
    private final JList<ChatItem> memberList;
    private final DefaultListModel<ChatItem> listModel;
    private final JButton confirmButton;
    private final List<Integer> selectedMemberIds; // 存储选中成员的ID
    private final List<String> selectedMemberNames;
    private final JTextArea selectedNamesTextArea;
    private static final Map<String, Image> imageCache = new HashMap<>();

    public GroupMemberSelectPanel(List<ChatItem> allMembers) {
        this.selectedNamesTextArea = new JTextArea();
        this.selectedMemberIds = new ArrayList<>();
        this.selectedMemberNames = new ArrayList<>();
        setLayout(new BorderLayout());

        // 顶部标题
        JLabel titleLabel = new JLabel("选择群成员", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // 成员列表
        listModel = new DefaultListModel<>();
        for (ChatItem member : allMembers) {
            listModel.addElement(member);
        }

        memberList = new JList<>(listModel);
        memberList.setCellRenderer(new ChatItemRenderer());
        memberList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // 禁用默认选择效果
        memberList.setSelectionBackground(memberList.getBackground());
        memberList.setSelectionForeground(memberList.getForeground());

        // 添加鼠标点击监听器处理选择逻辑
        memberList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = memberList.locationToIndex(e.getPoint());
                if (index >= 0) {
                    ChatItem selected = listModel.getElementAt(index);
                    int memberId = selected.getReceiverId();
                    String memberName = selected.getReceiverName();
                    // 切换选中状态
                    if (selectedMemberIds.contains(memberId)) {
                        selectedMemberIds.remove(Integer.valueOf(memberId));
                        selectedMemberNames.remove(memberName);
                    } else {
                        selectedMemberIds.add(memberId);
                        selectedMemberNames.add(memberName);
                    }

                    // 更新成员列表显示
                    memberList.repaint();

                    // 显示选中成员的名字，直接使用 selectedMemberNames
                    StringBuilder names = new StringBuilder();
                    for (String name : selectedMemberNames) {
                        names.append(name).append("\n");
                    }
                    selectedNamesTextArea.setText(names.toString());
                    // 确保文本区域重绘以显示最新内容
                    selectedNamesTextArea.repaint();
                }
            }
        });

        add(new JScrollPane(memberList), BorderLayout.CENTER);

        // 显示选中成员名字的文本区域
        //selectedNamesTextArea.setEditable(false);
        add(new JScrollPane(selectedNamesTextArea), BorderLayout.SOUTH);

        // 底部确认按钮
        confirmButton = new JButton("创建群聊");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(confirmButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JButton getConfirmButton() {
        return confirmButton;
    }

    public List<Integer> getSelectedMemberIds() {
        return new ArrayList<>(selectedMemberIds); // 返回副本保护内部数据
    }

    public List<String> getSelectedMemberNames() {
        return new ArrayList<>(selectedMemberNames); // 返回副本保护内部数据
    }

    public Set<Integer> getSelectedMembers() {
        List<ChatItem> selected = new ArrayList<>();
        Set<Integer>selectedIDs=new HashSet<>();
        for (int i = 0; i < listModel.size(); i++) {
            ChatItem member = listModel.getElementAt(i);
            if (selectedMemberIds.contains(member.getReceiverId())) {
                selected.add(member);
                selectedIDs.add(member.getReceiverId());
            }
        }
        return selectedIDs;
    }

    class ChatItemRenderer extends JPanel implements ListCellRenderer<ChatItem> {
        private final JLabel iconLabel = new JLabel();
        private final JLabel titleLabel = new JLabel();
        private final JLabel timeLabel = new JLabel();
        private final JLabel previewLabel = new JLabel();
        private JLabel unreadLabel;
        private JPanel leftPanel;

        public ChatItemRenderer() {
            setLayout(new BorderLayout(15, 0));
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

            leftPanel = new JPanel(new BorderLayout(10, 0));
            leftPanel.add(iconLabel, BorderLayout.WEST);

            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setOpaque(false);

            titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
            previewLabel.setFont(new Font("Noto Sans CJK SC", Font.PLAIN, 12));
            previewLabel.setForeground(new Color(150, 150, 150));

            textPanel.add(titleLabel);
            textPanel.add(Box.createVerticalStrut(5));
            textPanel.add(previewLabel);
            leftPanel.add(textPanel, BorderLayout.CENTER);

            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.setOpaque(false);

            timeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
            timeLabel.setForeground(new Color(0, 0, 0));

            rightPanel.add(Box.createVerticalStrut(25));
            rightPanel.add(timeLabel);

            add(leftPanel, BorderLayout.CENTER);
            add(rightPanel, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends ChatItem> list,
                                                      ChatItem item,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus) {

            titleLabel.setText(item.getReceiverName());

            previewLabel.setText(item.getPreview().length() > 20?
                    item.getPreview().substring(0, 17) + "..." :
                    item.getPreview());

            // 加载头像
            if (item.getAvatarPath() != null) {
                Image image = imageCache.get(item.getAvatarPath());
                if (image == null) {
                    try {
                        BufferedImage original = ImageIO.read(new File(item.getAvatarPath()));
                        image = original.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                        imageCache.put(item.getAvatarPath(), image);
                    } catch (IOException e) {
                        image = imageCache.get(DEFAULT_AVATAR);
                    }
                }
                iconLabel.setIcon(new ImageIcon(image));
            } else {
                iconLabel.setIcon(new ImageIcon(imageCache.get(DEFAULT_AVATAR)));
            }

            // 根据选中状态设置 leftPanel 的颜色
            if (GroupMemberSelectPanel.this.selectedMemberIds.contains(item.getReceiverId())) {
                leftPanel.setBackground(new Color(0, 255, 0)); // 绿色背景表示选中
                int sort=GroupMemberSelectPanel.this.selectedMemberIds.indexOf(item.getReceiverId());
                timeLabel.setText(String.valueOf(sort+1));
            } else {
                leftPanel.setBackground(list.getBackground());
                int sort=GroupMemberSelectPanel.this.selectedMemberIds.indexOf(item.getReceiverId());
                timeLabel.setText("");
            }

            return this;
        }
    }
}