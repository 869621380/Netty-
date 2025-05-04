package org.example.View;

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

    /**
     *
     * @param senderId 发送方ID
     * @param receiverId 接收方ID
     */
    public ChatWindow(int senderId, int receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        InitLayout();

    }

    private void InitLayout() {
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
        if(listener != null) {
            listener.setInitData(senderId, receiverId);
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
        if(status.equals("在线"))statusLabel.setForeground(Color.GREEN);
        else statusLabel.setForeground(Color.RED);
    }

    public void addMessage(SingleChatMessage content) {

         MessagePanel messagePanel = new MessagePanel(
                 Objects.equals(senderId,content.getSenderID()) ?senderAvatar:receiverAvatar,
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


    public void setAvatar(BufferedImage senderAvatar,BufferedImage receiverAvatar) {
        this.senderAvatar=senderAvatar;
        this.receiverAvatar=receiverAvatar;
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

    private void addListener(){

        //发送消息按钮监听器
        sendButton.addActionListener(e->sendMessage());

        // 表情按钮事件监听器
        emojiButton.addActionListener(e -> {
            emojiMenu.show(emojiButton, 0, emojiButton.getHeight());
        });

        // 输入框 Enter 键监听器
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    sendMessage();
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
                            SingleChatMessage message=new SingleChatMessage(
                                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")),
                                    SingleChatMessage.SENDING,senderId,receiverId,"image",fileImage);
                            addMessage(message);
                            listener.sendMessage(message);
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
    private void sendMessage(){
        if(listener!=null) {
            SingleChatMessage singleChatMessage =
                    new SingleChatMessage(
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")),
                            SingleChatMessage.SENDING,senderId,receiverId,"text",inputField.getText()
                    );
            addMessage(singleChatMessage);
            inputField.setText("");
            listener.sendMessage(singleChatMessage);
        }
    }

    public interface ChatMessageListener {
        void setInitData(Integer senderId,Integer receiverId);
        void sendMessage(SingleChatMessage content);
        void flushLoginStatus(Integer receiverId);
        void getReceiverLoginStatus(Integer receiverId);
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag&&listener!=null) {
            listener.flushLoginStatus(receiverId);
        }
    }
}
