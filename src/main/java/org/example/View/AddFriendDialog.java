package org.example.View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddFriendDialog extends JDialog {
    private JTextField friendIdField;
    private JButton searchButton;
    private JButton addButton;
    private JLabel statusLabel;
    private JLabel nameLabel;
    private JLabel avatarLabel;
    private AddFriendListener listener;
    private Integer currentUserId;
    private Integer foundFriendId;
    private boolean friendFound = false;

    public AddFriendDialog(Frame owner, Integer userId) {
        super(owner, "添加好友", true);
        this.currentUserId = userId;
        initComponents();
        layoutComponents();
        addListeners();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        friendIdField = new JTextField(15);
        searchButton = new JButton("搜索");
        addButton = new JButton("添加为好友");
        addButton.setEnabled(false);
        statusLabel = new JLabel("请输入要添加的好友ID");
        statusLabel.setForeground(Color.GRAY);
        nameLabel = new JLabel();
        avatarLabel = new JLabel();
        avatarLabel.setPreferredSize(new Dimension(40, 40));
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        // 搜索面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.add(new JLabel("好友ID:"));
        searchPanel.add(friendIdField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // 信息展示面板
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        userInfoPanel.add(avatarLabel);
        userInfoPanel.add(nameLabel);

        infoPanel.add(userInfoPanel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(statusLabel);
        add(infoPanel, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(addButton);
        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void addListeners() {
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Integer friendId = Integer.parseInt(friendIdField.getText().trim());
                    if (friendId.equals(currentUserId)) {
                        statusLabel.setText("不能添加自己为好友");
                        statusLabel.setForeground(Color.RED);
                        addButton.setEnabled(false);
                        nameLabel.setText("");
                        avatarLabel.setIcon(null);
                        friendFound = false;
                        return;
                    }

                    if (listener != null) {
                        listener.onSearchFriend(friendId, new SearchFriendCallback() {
                            @Override
                            public void onSuccess(String friendName, ImageIcon avatar) {
                                nameLabel.setText(friendName);
                                if (avatar != null) {
                                    avatarLabel.setIcon(avatar);
                                }
                                statusLabel.setText("找到用户");
                                statusLabel.setForeground(new Color(0, 150, 0));
                                addButton.setEnabled(true);
                                foundFriendId = friendId;
                                friendFound = true;
                            }

                            @Override
                            public void onFailure(String message) {
                                nameLabel.setText("");
                                avatarLabel.setIcon(null);
                                statusLabel.setText(message);
                                statusLabel.setForeground(Color.RED);
                                addButton.setEnabled(false);
                                friendFound = false;
                            }
                        });
                    }
                } catch (NumberFormatException ex) {
                    statusLabel.setText("请输入有效的用户ID");
                    statusLabel.setForeground(Color.RED);
                    addButton.setEnabled(false);
                    nameLabel.setText("");
                    avatarLabel.setIcon(null);
                    friendFound = false;
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (friendFound && listener != null) {
                    listener.onAddFriend(currentUserId, foundFriendId, new AddFriendCallback() {
                        @Override
                        public void onSuccess() {
                            JOptionPane.showMessageDialog(AddFriendDialog.this,
                                    "好友添加成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        }

                        @Override
                        public void onFailure(String message) {
                            statusLabel.setText(message);
                            statusLabel.setForeground(Color.RED);
                        }
                    });
                }
            }
        });
    }

    public void setAddFriendListener(AddFriendListener listener) {
        this.listener = listener;
    }

    private void setBorder(javax.swing.border.Border border) {
        ((JPanel)getContentPane()).setBorder(border);
    }

    public interface AddFriendListener {
        void onSearchFriend(Integer friendId, SearchFriendCallback callback);
        void onAddFriend(Integer userId, Integer friendId, AddFriendCallback callback);
    }

    public interface SearchFriendCallback {
        void onSuccess(String friendName, ImageIcon avatar);
        void onFailure(String message);
    }

    public interface AddFriendCallback {
        void onSuccess();
        void onFailure(String message);
    }
}