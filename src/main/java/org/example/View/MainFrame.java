package org.example.View;


import io.netty.channel.ChannelHandlerContext;
import org.example.Cache.MessageCache;
import org.example.Controller.ChatListController;
import org.example.Controller.ChatWindowMessageController;
import org.example.View.ChatWindow;
import org.example.View.ChatListPanel;

import javax.swing.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainFrame extends JFrame {

    private ChatWindow currentChatWindow = null;
    private ChatListController chatListController;
    private Map<Integer,ChatWindow> chatWindowMap;
    private Map<Integer,ChatWindowMessageController>chatWindowMessageControllerMap;
    private ChatWindow currentWindow;
    public MainFrame(Integer userId) {
        setTitle("Main Frame");
        LocalDateTime now = LocalDateTime.now();
        setSize(925,650);
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 确保关闭窗口时退出程序
        ChatListPanel chatListPanel = new ChatListPanel(userId);
        chatListController = new ChatListController(chatListPanel);
        chatListPanel.setVisible(true);
        chatListPanel.setBounds(50,-5,240,650);
        add(chatListPanel);
        new Thread(()-> {
            try {
                chatListController.getLatch().await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Map<Integer,ChatWindow>chatWindowMap=chatListPanel.getChatWindowMap();
            chatWindowMessageControllerMap=chatListPanel.getChatWindowMessageControllerMap();
            MessageCache.setChatWindowMessageControllerMap(chatWindowMessageControllerMap);
            for(Map.Entry<Integer,ChatWindow> entry:chatWindowMap.entrySet()){
                entry.getValue().setBounds(300, 0, 610, 613);
                add(entry.getValue());
            }
            revalidate();
            repaint();
        }).start();
        LocalDateTime now2 = LocalDateTime.now();
        Duration duration = Duration.between(now2,now);
        System.out.println(duration.toMillis());


    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainFrame(1);
            }
        });
    }

    public void addCtx(ChannelHandlerContext ctx){
        chatListController.setCtx(ctx);
        new Thread(()-> {
            try {
                chatListController.getLatch().await();
                for(Map.Entry<Integer,ChatWindowMessageController> entry:chatWindowMessageControllerMap.entrySet()){
                    entry.getValue().setCtx(ctx);
                }
            }catch(InterruptedException e){}
        }).start();


    }
}

