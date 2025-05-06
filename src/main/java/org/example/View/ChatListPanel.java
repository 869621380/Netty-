package org.example.View;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;
import org.example.Controller.ChatWindowMessageController;
import org.example.Model.Domain.ChatItem;
import org.example.Util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;


public class ChatListPanel extends JPanel {
    private static final Logger log = LoggerFactory.getLogger(ChatListPanel.class);
    //用户id
    Integer userId;
    //聊天列表
    private JList<ChatItem> list;
    //关联列表
    private DefaultListModel<ChatItem> listModel;
    @Getter
    private Map<Object, ChatWindow> chatWindowMap;
    @Getter
    @Setter
    List<ChatWindow> chatWindowList;

    public void setCurrentChatWindow(ChatWindow currentChatWindow) {
        this.currentChatWindow = currentChatWindow;
    }

    public ChatWindow getCurrentChatWindow() {
        return currentChatWindow;
    }

    ChatWindow currentChatWindow;
    @Getter
    Map<Object, ChatWindowMessageController> chatWindowMessageControllerMap;
    //    ChatWindowMessageController chatWindowMessageController;
    // 图片缓存
    private static final Map<String, Image> imageCache = new HashMap<>();

    //监听器
    private ChatListListener listener;

    private CreateGroupListener createGroupListener;

    public ChatListPanel(Integer userId) {
        this.userId = userId;
        //  chatWindowMessageController=new ChatWindowMessageController(null);
        chatWindowMessageControllerMap = new HashMap<>();
        try {
            BufferedImage original = ImageIO.read(new File(Constants.DEFAULT_AVATAR));
            Image image = original.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            imageCache.put(Constants.DEFAULT_AVATAR, image);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        InitLayout();

    }

    public void addChatItem(List<ChatItem> chatItems) {
        if (chatItems != null) {
            for (ChatItem chatItem : chatItems) {
                listModel.addElement(chatItem);
            }
        }

    }

    void InitLayout() {
        setLayout(new BorderLayout());

        // 添加顶部工具栏
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // 创建群聊按钮
        JButton createGroupBtn = new JButton("创建群聊");
        createGroupBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        createGroupBtn.setFocusPainted(false);
        createGroupBtn.addActionListener(e -> {
            if (createGroupListener != null) {
                createGroupListener.onCreateGroupRequested(userId);
            }
        });
        toolBar.add(createGroupBtn);
        add(toolBar, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setCellRenderer(new ChatItemRenderer());
        list.setSelectionBackground(new Color(219, 219, 219));
        list.setFixedCellHeight(80);
        list.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // 添加鼠标点击监听器
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = list.locationToIndex(e.getPoint());
                if (index >= 0) {
                    listModel.getElementAt(index).setUnreadCount(0); //点击时将未读消息数设为0
                    //      ChatItem item = list.getModel().getElementAt(index);
                    ChatWindow chatWindow = chatWindowMap.get(0);
                    //在mapper中，单聊键为id，群聊键为name
                    if (list.getModel().getElementAt(index).getReceiverType() == 0) {
                        chatWindow = chatWindowMap.get(list.getModel().getElementAt(index).getReceiverId());
                    } else {
                        chatWindow=chatWindowMap.get(list.getModel().getElementAt(index).getReceiverName());
                    }
                    if (currentChatWindow == null) {
                        currentChatWindow = chatWindow;
                        currentChatWindow.setVisible(true);
                    } else if (currentChatWindow != chatWindow) {
                        currentChatWindow.setVisible(false);
                        currentChatWindow = chatWindow;
                        currentChatWindow.setVisible(true);
                    }
                    revalidate();
                    repaint();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(list);
        add(scrollPane, BorderLayout.CENTER);


        chatWindowMap = new HashMap<>();

    }

    //设定chatlistListener监听器，调用setInitData，传入用户id
    public void setChatListener(ChatListListener listener) {
        this.listener = listener;
        if (listener != null) {
            listener.setInitData(userId);
        }
    }

    // 设置群聊创建监听器
    public void setCreateGroupListener(CreateGroupListener listener) {
        this.createGroupListener = listener;
    }

    public void updateItem(Integer receiverId, String content) {
        for (int i = 0; i < listModel.size(); i++) {
            ChatItem chatItem = listModel.get(i);
            if (chatItem.getReceiverId().equals(receiverId)) {
                chatItem.setPreview(content);
                chatItem.setPreviewTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
                break;
            }
        }
    }


    static class ChatItemRenderer extends JPanel implements ListCellRenderer<ChatItem> {
        private final JLabel iconLabel = new JLabel();
        private final JLabel titleLabel = new JLabel();
        private final JLabel timeLabel = new JLabel();
        private final JLabel previewLabel = new JLabel();
        private JLabel unreadLabel;

        public ChatItemRenderer() {
            setLayout(new BorderLayout(15, 0));
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

            JPanel leftPanel = new JPanel(new BorderLayout(10, 0));
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

            timeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
            timeLabel.setForeground(new Color(140, 140, 140));

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
            try {
                if (item.getPreviewTime() != null) {
                    Date itemDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(String.valueOf(item.getPreviewTime()));
                    Calendar itemCalendar = Calendar.getInstance();
                    itemCalendar.setTime(itemDate);

                    Calendar currentCalendar = Calendar.getInstance();

                    if (itemCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)) {
                        if (itemCalendar.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR)) {
                            timeLabel.setText(new SimpleDateFormat("HH:mm").format(itemDate));
                        } else {
                            timeLabel.setText(new SimpleDateFormat("MM-dd").format(itemDate));
                        }
                    } else {
                        timeLabel.setText(new SimpleDateFormat("yyyy-MM").format(itemDate));
                    }
                } else timeLabel.setText("");

            } catch (ParseException e) {
                timeLabel.setText(String.valueOf(item.getPreviewTime()));
            }

            previewLabel.setText(item.getPreview().length() > 20 ?
                    item.getPreview().substring(0, 17) + "..." :
                    item.getPreview());

            //加载头像
            if (item.getAvatarPath() != null) {
                Image image = imageCache.get(item.getAvatarPath());
                if (image == null) {
                    try {
                        BufferedImage original = ImageIO.read(new File(item.getAvatarPath()));
                        image = original.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                        imageCache.put(item.getAvatarPath(), image);
                    } catch (IOException e) {
                        image = imageCache.get(Constants.DEFAULT_AVATAR);
                    }
                }
                iconLabel.setIcon(new ImageIcon(image));
            } else {
                iconLabel.setIcon(new ImageIcon(imageCache.get(Constants.DEFAULT_AVATAR)));
            }


            if (item.getUnreadCount() > 0) {
                if (unreadLabel == null) {
                    unreadLabel = createUnreadCountLabel(item.getUnreadCount());
                    ((JPanel) getComponent(1)).add(unreadLabel, 0);
                } else {
                    unreadLabel.setText(item.getUnreadCount() > 99 ? "99+" : String.valueOf(item.getUnreadCount()));
                }
                unreadLabel.setVisible(true);
            } else if (unreadLabel != null) {
                unreadLabel.setVisible(false);
            }

            setBackground(isSelected ?
                    new Color(219, 219, 219) :
                    list.getBackground());
            LocalDateTime time2 = LocalDateTime.now();

            return this;
        }

        private JLabel createUnreadCountLabel(int count) {
            JLabel label = new JLabel(count > 99 ? "99+" : String.valueOf(count));
            label.setOpaque(true);
            label.setBackground(new Color(255, 68, 68));
            label.setForeground(Color.WHITE);
            label.setFont(new Font("微软雅黑", Font.BOLD, 12));
            label.setBorder(BorderFactory.createEmptyBorder(2, 6, 0, 6));

            label.setUI(new javax.swing.plaf.basic.BasicLabelUI() {
                public void paint(Graphics g, JComponent c) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(c.getBackground());
                    g2.fillOval(0, 0, c.getWidth(), c.getHeight());
                    super.paint(g2, c);
                    g2.dispose();
                }
            });
            return label;
        }
    }

    public interface ChatListListener {
        void setInitData(Integer userId);

    }

    // 添加群聊创建监听器接口
    public interface CreateGroupListener {
        void onCreateGroupRequested(Integer userId);
    }

    //增加群聊Window创建途径
    public void addChatWindow(List<ChatItem> chatItems) {

        for (ChatItem chatItem : chatItems) {
            ChatWindow chatWindow = new ChatWindow(userId, chatItem.getReceiverId(),chatItem.getReceiverName());
            if (chatItem.getReceiverId() != -1) {  //单聊
                chatWindowMap.put(chatItem.getReceiverId(), chatWindow);
                //  chatWindowMessageController.setView(chatWindow);
                ChatWindowMessageController chatWindowMessageController = new ChatWindowMessageController(chatWindow);
                chatWindowMessageControllerMap.put(chatItem.getReceiverId(), chatWindowMessageController);
                chatWindow.setVisible(false);
            } else {  //群聊
                chatWindowMap.put(chatItem.getReceiverName(), chatWindow);
                ChatWindowMessageController chatWindowMessageController = new ChatWindowMessageController(chatWindow);
                chatWindowMessageControllerMap.put(chatItem.getReceiverName(), chatWindowMessageController);
                chatWindow.setVisible(false);
            }
        }

    }

}