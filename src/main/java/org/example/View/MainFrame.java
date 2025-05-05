package org.example.View;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import org.example.Cache.MessageCache;
import org.example.Controller.ChatListController;
import org.example.Controller.ChatWindowMessageController;
import org.example.Util.ThreadPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

public class MainFrame extends JFrame {

    // 在类顶部添加以下代码
    private static final Logger logger = LoggerFactory.getLogger(MainFrame.class);
    //单例实现
    private static MainFrame instance;

    private Integer userId;
    private GroupsPanel groupsPanel;
    private FriendsPanel friendsPanel;

    private ChatWindow currentChatWindow = null;
    private ChatListController chatListController;
    private Map<Integer,ChatWindow> chatWindowMap;
    private Map<Integer,ChatWindowMessageController>chatWindowMessageControllerMap;

    //是否联网
    @Getter
    boolean isCtx;

//    public MainFrame(Integer userId) {
//        this.userId = userId;
//        setTitle("聊天客户端");
//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                chatListController.closeCtx();
//                super.windowClosing(e);
//            }
//        });
//        setSize(925, 650);
//        setLayout(null);
//        setVisible(true);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        // 左侧面板区域
//        JTabbedPane leftTabPane = new JTabbedPane();
//        leftTabPane.setBounds(20, 10, 270, 600);
//
//        // 添加聊天列表面板到标签页
//        ChatListPanel chatListPanel = new ChatListPanel(userId);
//        chatListController = new ChatListController(chatListPanel);
//        leftTabPane.addTab("聊天", null, chatListPanel, "显示最近的聊天记录");
//
//        // 添加好友列表面板到标签页
//        this.friendsPanel = new FriendsPanel();
//        leftTabPane.addTab("好友", null, friendsPanel, "显示您的好友列表");
//
//        // 添加群组面板到标签页
//        this.groupsPanel = new GroupsPanel();
//        leftTabPane.addTab("群组", null, groupsPanel, "显示您加入的群组");
//
//        add(leftTabPane);
//
//        new Thread(()-> {
//            try {
//                chatListController.getLatch().await();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            chatWindowMap = chatListPanel.getChatWindowMap();
//            chatWindowMessageControllerMap = chatListPanel.getChatWindowMessageControllerMap();
//            MessageCache.setChatWindowMessageControllerMap(chatWindowMessageControllerMap);
//            MessageCache.setChatListController(chatListController);
//            for(Map.Entry<Integer,ChatWindow> entry:chatWindowMap.entrySet()){
//                entry.getValue().setBounds(300, 10, 590, 600);
//                add(entry.getValue());
//            }
//            revalidate();
//            repaint();
//        }).start();
//    }

    // 在 MainFrame 构造函数中
    public MainFrame(Integer userId) {
        this.userId = userId;
        setTitle("聊天客户端");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                chatListController.closeCtx();
                super.windowClosing(e);
            }
        });
        setSize(925, 650);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 左侧面板区域
        JTabbedPane leftTabPane = new JTabbedPane();
        leftTabPane.setBounds(20, 10, 270, 600);

        // 添加聊天列表面板到标签页
        ChatListPanel chatListPanel = new ChatListPanel(userId);
        chatListController = new ChatListController(chatListPanel);
        leftTabPane.addTab("聊天", null, chatListPanel, "显示最近的聊天记录");

        // 添加好友列表面板到标签页
        this.friendsPanel = new FriendsPanel();
        leftTabPane.addTab("好友", null, friendsPanel, "显示您的好友列表");

        // 添加群组面板到标签页
        this.groupsPanel = new GroupsPanel();
        leftTabPane.addTab("群组", null, groupsPanel, "显示您加入的群组");

        add(leftTabPane);

        new Thread(()-> {
            try {
                chatListController.getLatch().await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            chatWindowMap = chatListPanel.getChatWindowMap();
            chatWindowMessageControllerMap = chatListPanel.getChatWindowMessageControllerMap();
            MessageCache.setChatWindowMessageControllerMap(chatWindowMessageControllerMap);
            MessageCache.setChatListController(chatListController);
            for(Map.Entry<Integer,ChatWindow> entry:chatWindowMap.entrySet()){
                entry.getValue().setBounds(300, 10, 590, 600);
                this.add(entry.getValue());
            }
        }).start();

        setLocationRelativeTo(null); // 窗口居中显示
        setVisible(true);
    }

    // 获取单例实例的静态方法
    public static synchronized MainFrame getInstance() {
        if (instance == null) {
            throw new IllegalStateException("MainFrame has not been initialized yet. Call createInstance first.");
        }
        return instance;
    }

    // 创建实例的方法
    public static synchronized MainFrame createInstance(Integer userId) {
        if (instance == null) {
            instance = new MainFrame(userId);
        }
        return instance;
    }

    // 添加打开好友聊天窗口的方法
    public void openChatWithFriend(int friendId, String friendName) {
        // 显示或创建聊天窗口
        if (chatWindowMap != null && chatWindowMap.containsKey(friendId)) {
            // 如果已有窗口，则显示
            if (currentChatWindow != null) {
                currentChatWindow.setVisible(false);
            }
            ChatWindow window = chatWindowMap.get(friendId);
            window.setVisible(true);
            currentChatWindow = window;
        } else {
            // 这种情况不应该发生，因为聊天窗口应该在初始化时创建
            JOptionPane.showMessageDialog(this,
                    "未找到与 " + friendName + " 的聊天窗口。",
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 添加刷新好友列表的方法
    public void refreshFriendList() {
        if (friendsPanel != null) {
            SwingUtilities.invokeLater(() -> {
                friendsPanel.loadFriends();
            });
        }
    }

    // 添加设置GroupsPanel的方法
    public void setGroupsPanel(GroupsPanel panel) {
        this.groupsPanel = panel;
    }

    // 添加设置FriendsPanel的方法
    public void setFriendsPanel(FriendsPanel panel) {
        this.friendsPanel = panel;
    }

    /**
     * 刷新群组列表
     */
    public void refreshGroupList() {
        if (groupsPanel != null) {
            SwingUtilities.invokeLater(() -> {
                try {
                    groupsPanel.loadGroups();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                            "刷新群组列表失败: " + e.getMessage(),
                            "错误", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createInstance(1);
        });
    }

    public void addCtx(ChannelHandlerContext ctx){
        isCtx=true;
        chatListController.setCtx(ctx);
        ThreadPoolManager.getDBExecutorService().execute(() -> {
            try {
                chatListController.getLatch().await();
                for(Map.Entry<Integer,ChatWindowMessageController> entry:chatWindowMessageControllerMap.entrySet()){
                    entry.getValue().setCtx(ctx);
                }
            }catch(InterruptedException e){}
        });
    }

    public void removeCtx(){
        isCtx=false;
        chatListController.setCtx(null);
        for(Map.Entry<Integer,ChatWindowMessageController> entry:chatWindowMessageControllerMap.entrySet()){
            entry.getValue().setCtx(null);
        }
    }

    /**
     * 显示群组信息
     * @param groupId 群组ID
     * @param groupName 群组名称
     */
    public void openGroupChatWindow(String groupId, String groupName) {
        // 使用消息框显示群组信息，而不是打开聊天窗口
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this,
                    "群组名称: " + groupName + "\n" +
                            "群组ID: " + groupId + "\n\n" +
                            "当前版本不支持群聊功能。",
                    "群组信息",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    /**
     * 重连后恢复UI状态
     */
    /**
     * 重连后恢复UI状态
     */
    public void reconnectUI() {
        try {
            // 重新加载聊天列表 - 使用已有方法或简化处理
            if (chatListController != null) {
                // 如果不存在loadChats()方法，使用已有的refreshChatList()方法或其他方法
                //chatListController.refreshChatList();  // 或者使用其他已存在的方法
            }

            // 刷新当前打开的聊天窗口
            if (currentChatWindow != null) {
                // 如果ChatWindow没有getFriendId()方法，使用其他方式获取friendId
                // 例如通过遍历chatWindowMap找到当前窗口对应的key
                Integer friendId = null;
                for (Map.Entry<Integer, ChatWindow> entry : chatWindowMap.entrySet()) {
                    if (entry.getValue() == currentChatWindow) {
                        friendId = entry.getKey();
                        break;
                    }
                }

                if (friendId != null) {
                    // 获取聊天控制器
                    ChatWindowMessageController controller = chatWindowMessageControllerMap.get(friendId);
                    if (controller != null) {
                        // 如果没有loadMessages()方法，使用现有的刷新消息方法
                        // 或者简单地重新设置ctx
                        //controller.setCtx(chatListController.getCtx());
                        // 如果有其他刷新消息的方法可以调用它
                        // controller.refreshMessages();
                        // 确保窗口可见
                        currentChatWindow.setVisible(true);
                        add(currentChatWindow);
                        revalidate();
                        repaint();
                    }

                    // 确保窗口可见
                    currentChatWindow.setVisible(true);
                    add(currentChatWindow);
                    revalidate();
                    repaint();
                }
            }

            // 设置连接状态标志
            isCtx = true;

            System.out.println("UI状态已在重连后恢复");  // 使用System.out.println代替logger
        } catch (Exception e) {
            System.err.println("恢复UI状态失败: " + e.getMessage());  // 使用System.err.println代替logger
            e.printStackTrace();
        }
    }
}