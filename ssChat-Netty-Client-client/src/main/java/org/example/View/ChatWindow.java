package org.example.View;

import org.example.Model.Domain.GroupChatMessage;
import org.example.Model.Domain.Message;
import org.example.Model.Domain.SingleChatMessage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ChatWindow extends JLabel {
    //接收方名称
    private JLabel receiverNameLabel;
    //接收方登陆状态
    JLabel statusLabel;
    //聊天消息页面主题
    private JPanel chatPanel;
    //输入框
    private JTextField inputField;
    //发送按钮
    JButton sendButton;
    //聊天框
    public JScrollPane scrollPane;
    //表情按钮
    private JButton emojiButton;
    //表情菜单
    private JPopupMenu emojiMenu;
    //监听器
    private ChatMessageListener listener;

    private final Integer senderId;
    private final Integer receiverId;
    //头像
    private BufferedImage senderAvatar;
    private BufferedImage receiverAvatar;
    //文件
    JButton fileButton;
    //接收人名称
    private String receiverName;

    /**
     * @param senderId   发送方ID
     * @param receiverId 接收方ID
     */
    public ChatWindow(int senderId, int receiverId, String receiverName) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        InitLayout();

    }

    private void InitLayout() {
        //System.out.println("新群聊"+receiverName+"初始化中");
        setPreferredSize(new Dimension(615, 650));
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        receiverNameLabel = new JLabel();
        statusLabel = new JLabel();


        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(statusLabel);

        headerPanel.add(receiverNameLabel, BorderLayout.WEST);
        headerPanel.add(statusPanel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // 聊天消息面板
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        chatPanel.add(Box.createVerticalGlue());

        // 滚动面板
        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        // 输入面板
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("发送");
        fileButton = new JButton("发送文件/图片/视频");
        emojiButton = new JButton("😀");

        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(emojiButton);
        buttonPanel.add(sendButton);

        inputPanel.add(buttonPanel, BorderLayout.EAST);
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(fileButton, BorderLayout.SOUTH);
        add(inputPanel, BorderLayout.SOUTH);


        // 初始化表情菜单
        initEmojiMenu();

        addListener();
        System.out.println("初始化完成");
    }

    private void initEmojiMenu() {
        emojiMenu = new JPopupMenu();
        JPanel emojiPanel = new JPanel(new GridLayout(0, 10));

        String[] emojis = {
                "😀", "😁", "😂", "🤣", "😃", "😄", "😅", "😆", "😉", "😊",
                "😋", "😎", "😍", "😘", "😗", "😙", "😚", "🙂", "🤗", "🤔",
                "🤨", "😐", "😑", "😶", "🙄", "😏", "😣", "😥", "😮", "🤐",
                "😯", "😪", "😫", "😴", "😌", "😛", "😜", "😝", "🤤", "😒",
                "😓", "😔", "😕", "🙃", "🤑", "😲", "☹", "🙁", "😖", "😞",
                "😟", "😤", "😢", "😭", "😦", "😧", "😨", "😩", "🤯", "😬",
                "😰", "😱", "😳", "🤪", "😵", "😡", "😠", "🤬", "😷", "🤒"
        };

        for (String emoji : emojis) {
            JButton emojiButton = new JButton(emoji);
            emojiButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
            emojiButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            emojiButton.setContentAreaFilled(false);
            emojiButton.addActionListener(e -> {
                String currentText = inputField.getText();
                int pos = inputField.getCaretPosition();
                inputField.setText(currentText.substring(0, pos) + emoji + currentText.substring(pos));
                inputField.setCaretPosition(pos + emoji.length());
            });
            emojiPanel.add(emojiButton);
        }

        emojiMenu.add(emojiPanel);

        // 修改按钮点击事件
        emojiButton.addActionListener(e -> {

            Dimension menuSize = emojiMenu.getPreferredSize();
            Point buttonLoc = emojiButton.getLocationOnScreen();
            int x = 0;
            int y = -menuSize.height;

            // 如果会超出屏幕上方，改为向下弹出
            if (buttonLoc.y - menuSize.height < 0) {
                y = emojiButton.getHeight();
            }

            emojiMenu.show(emojiButton, x, y);
        });
    }

    public void setChatWindowMessageListener(ChatMessageListener listener) {
        this.listener = listener;
        if (listener != null) {
            if (this.receiverId != -1) {
                listener.setInitData(senderId, receiverId);
            } else {
                listener.setGroupInitData(senderId, receiverName);
            }
            listener.getReceiverLoginStatus(receiverId);
            SwingUtilities.invokeLater(() -> {
                JScrollBar vertical = scrollPane.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            });
        }
    }

    public void setReceiverNameLabel(String receiverName) {
        receiverNameLabel.setText(receiverName);
    }

    public void setStatusLabel(String status) {
        statusLabel.setText(status);
        if (status.equals("在线")) statusLabel.setForeground(Color.GREEN);
        else statusLabel.setForeground(Color.RED);
    }

    public void addMessage(Message content) {
        //System.out.println("adding message: "+content.getContent());
        MessagePanel messagePanel = new MessagePanel(
                Objects.equals(senderId, content.getSenderID()) ? senderAvatar : receiverAvatar,
                content,
                senderId
        );

        chatPanel.add(messagePanel);
        chatPanel.add(Box.createVerticalGlue());

        // 更新UI
        chatPanel.revalidate();
        chatPanel.repaint();

        // 滚动到底部
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }


    public void setAvatar(BufferedImage senderAvatar, BufferedImage receiverAvatar) {
        this.senderAvatar = senderAvatar;
        this.receiverAvatar = receiverAvatar;
    }

    private boolean isImage(File file) {
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        return extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("gif");
    }

    private boolean isVideo(File file) {
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        return extension.equals("mp4") || extension.equals("avi") || extension.equals("mov");
    }

    private void addListener() {

        //发送消息按钮监听器
        if (receiverId != -1) {
            sendButton.addActionListener(e -> sendMessage());
        } else {
            sendButton.addActionListener(e ->sendGroupMessage());
        }

        // 表情按钮事件监听器
        emojiButton.addActionListener(e -> {
            emojiMenu.show(emojiButton, 0, emojiButton.getHeight());
        });

        // 输入框 Enter 键监听器
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    if(receiverId!=-1) sendMessage();
                    else sendGroupMessage();
                }
            }
        });

        // 文件/图片/视频发送按钮的事件监听器
        fileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (file != null) {
                    if (isImage(file)) {
                        try {
                            byte[] fileImage = imageFileToByteArray(file);

                            if(this.receiverId!=-1) {
                                SingleChatMessage message = new SingleChatMessage(
                                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")),
                                        SingleChatMessage.SENDING, senderId, receiverId, "image", fileImage);
                                addMessage(message);
                                listener.sendMessage(message);
                            }else{
                                System.out.println("准备发图片文件了嗷");
                                GroupChatMessage message = new GroupChatMessage(
                                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")),
                                        GroupChatMessage.SENDING, this.senderId, this.receiverName, "image", fileImage);

                                addMessage(message);
                                listener.sendGroupMessage(message);
                            }
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                    } else if (isVideo(file)) {
                        //        addMessage(new VideoMessageContent(file), true);
                    } else {
                        //        addMessage(new FileMessageContent(file), true);
                    }
                }
            }
        });
    }



    public static byte[] imageFileToByteArray(File file) throws IOException {
        BufferedImage image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 假设图像格式为 PNG，你可以根据实际情况修改
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    //发送消息逻辑，新增本地花存储
    private void sendMessage() {
        if (listener != null) {
            SingleChatMessage singleChatMessage =
                    new SingleChatMessage(
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")),
                            SingleChatMessage.SENDING, senderId, receiverId, "text", inputField.getText()
                    );
            addMessage(singleChatMessage);
            inputField.setText("");
            listener.sendMessage(singleChatMessage);
        }
    }
    //发送群消息
    private void sendGroupMessage() {
        if (listener != null) {
            GroupChatMessage groupChatMessage =
                    new GroupChatMessage(
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")),
                            SingleChatMessage.SENDING, senderId, receiverName, "text", inputField.getText()
                    );
            addMessage(groupChatMessage);
            inputField.setText("");
            listener.sendGroupMessage(groupChatMessage);
        }
    }


    public void moveToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }

    public interface ChatMessageListener {
        void setInitData(Integer senderId, Integer receiverId);

        void setGroupInitData(Integer senderId, String receiverName);

        void sendMessage(SingleChatMessage content);

        void sendGroupMessage(GroupChatMessage content);
        void flushLoginStatus(Integer receiverId);

        void getReceiverLoginStatus(Integer receiverId);
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag && listener != null) {
            listener.flushLoginStatus(receiverId);
        }
    }
}
