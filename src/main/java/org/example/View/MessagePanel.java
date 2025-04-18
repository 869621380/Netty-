package org.example.View;

import org.example.Model.Domain.SingleChatMessage;
import org.example.Model.message.requestMessage.ChatRequestMessage;
import org.example.Model.message.Message;
import org.example.Model.message.requestMessage.SingleChatRequestMessage;
import org.example.Model.message.requestMessage.SingleChatTextRequestMessage;
import org.example.Util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MessagePanel extends JPanel implements ActionListener {

    private static final int AVATAR_SIZE = 40;
    private static final Logger log = LoggerFactory.getLogger(MessagePanel.class);
    //判断是不是自己
    private final boolean isSelf;
    // 发送状态标签
    private JLabel statusLabel;
    //头像
    private JLabel avatarLabel;
    private SingleChatMessage content;

    /**
     *
     * @param avatarImage 发送方头像
     * @param content 内容
     * @param userId 当前登录账号的ID
     */
    public MessagePanel(BufferedImage avatarImage, SingleChatMessage content, int userId) {

        this.isSelf=content.getSenderID()==userId;
        this.content=content;
        content.addContentChangedListener(this);
        setLayout(avatarImage,content,userId);

    }

    void setLayout(BufferedImage avatarImage, SingleChatMessage content, int userId) {
        setLayout(null);
        setOpaque(false);
        // 头像标签
        avatarLabel=new JLabel();
        setAvatarLabel(avatarImage);
        add(avatarLabel);

        // 时间标签
        JLabel timeLabel = createTimeLabel(content.getSendTime());
        add(timeLabel);

        // 发送状态标签
        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 10)); // Windows
        if (isSelf) {
            setStatus(content.getSendStatus()); // 默认状态
            add(statusLabel);
        }
        // 消息气泡
        JPanel bubblePanel = getContentPanel(content,isSelf);
        add(bubblePanel);

        layoutComponents(avatarLabel, timeLabel, bubblePanel, Constants.MESSAGE_PANEL_WIDTH);
    }


    public void setAvatarLabel(BufferedImage avatarImage) {
        if(avatarImage != null) {
            avatarLabel.setIcon(new ImageIcon(avatarImage.getScaledInstance(AVATAR_SIZE, AVATAR_SIZE, Image.SCALE_SMOOTH)));
        }
        else{
            try {
                BufferedImage original = ImageIO.read(new File(Constants.DEFAULT_AVATAR));
                Image scaled = original.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                avatarLabel.setIcon(new ImageIcon(scaled.getScaledInstance(AVATAR_SIZE, AVATAR_SIZE, Image.SCALE_SMOOTH)));
            }catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        avatarLabel.setSize(AVATAR_SIZE, AVATAR_SIZE);
    }

    // 设置消息状态
    public void setStatus(String status) {
        if (!isSelf) return;
        statusLabel.setText(status);
        switch (status) {
            case "发送中", "已发送":
                statusLabel.setForeground(Color.GRAY);
                break;
            case "发送失败":
                statusLabel.setForeground(Color.RED);
                break;
        }
    }

    private JLabel createTimeLabel(String time) {
        JLabel label = new JLabel(time, SwingConstants.CENTER);
        label.setForeground(Color.GRAY);
        label.setFont(new Font("Arial", Font.PLAIN, 10));
        label.setSize(80, 15);
        return label;
    }

    private void layoutComponents(JLabel avatar, JLabel time, JPanel bubble, int width) {
        // 气泡尺寸
        bubble.setSize(bubble.getPreferredSize());

        if (isSelf) {
            avatar.setLocation(width - AVATAR_SIZE - 2, time.getHeight() + 2);
            bubble.setLocation(width - AVATAR_SIZE - bubble.getWidth() - 10, time.getHeight() + 2);
            time.setLocation(width - AVATAR_SIZE - bubble.getWidth()/2 - time.getWidth()/2 - 30, 0);
            statusLabel.setSize(40, 15);
            statusLabel.setLocation(width - AVATAR_SIZE - bubble.getWidth()/2 - time.getWidth()/2 +60, 0);
        } else {
            avatar.setLocation(2, time.getHeight() + 2);
            bubble.setLocation(AVATAR_SIZE + 6, time.getHeight() + 2);
            time.setLocation(AVATAR_SIZE + 6 + bubble.getWidth()/2 - time.getWidth()/2, 0);
        }
    }

    public JPanel getContentPanel(SingleChatMessage content, boolean isSelf) {

        if(content.getType().equals("text")) {
            return getTextContentPanel(content,isSelf);
        }
        return null;
    }

    private JPanel getTextContentPanel(SingleChatMessage content, boolean isSelf) {
        JTextArea messageTextArea = new JTextArea(content.getContent());
        messageTextArea.setEditable(false);
        messageTextArea.setLineWrap(true);
        messageTextArea.setWrapStyleWord(true);
        messageTextArea.setOpaque(false);
        messageTextArea.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel messageBubblePanel = new JPanel(new BorderLayout());
        messageBubblePanel.setOpaque(true);
        if (isSelf) {
            messageBubblePanel.setBackground(new Color(200, 255, 200));
            messageBubblePanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 255, 150)),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
        } else {
            messageBubblePanel.setBackground(Color.WHITE);
            messageBubblePanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
        }
        messageBubblePanel.add(messageTextArea, BorderLayout.CENTER);

        return messageBubblePanel;
    }
    @Override
    public Dimension getPreferredSize() {
        Component[] comps = getComponents();
        JPanel bubble = (JPanel)(isSelf ? comps[3] : comps[2]);
        JLabel time = (JLabel)comps[1];

        int height = time.getHeight() + bubble.getHeight() + 2;
        int width = 580;
        return new Dimension(width, height);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("contentChanged".equals(e.getActionCommand())) {
            // 移除旧的气泡面板
            Component[] comps = getComponents();
            JPanel oldBubble = (JPanel) (isSelf ? comps[3] : comps[2]);
            remove(oldBubble);

            // 创建新的气泡面板
            JPanel newBubble = getContentPanel(content, isSelf);
            add(newBubble);

            // 重新布局组件
            JLabel timeLabel = (JLabel) comps[1];
            layoutComponents(avatarLabel, timeLabel, newBubble, Constants.MESSAGE_PANEL_WIDTH);

            // 更新状态标签
            if (isSelf) {
                setStatus(content.getSendStatus());
            }

            // 重绘面板
            revalidate();
            repaint();
        }
    }
}
