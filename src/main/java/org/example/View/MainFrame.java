package org.example.View;


import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import org.example.Cache.MessageCache;
import org.example.Controller.ChatListController;
import org.example.Controller.ChatWindowMessageController;
import org.example.Util.ThreadPoolManager;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;
import java.util.Objects;

public class MainFrame extends JFrame {

    private ChatWindow currentChatWindow = null;
    private ChatListController chatListController;
    private Map<Integer,ChatWindow> chatWindowMap;
    private Map<Object,ChatWindowMessageController>chatWindowMessageControllerMap;
    //是否联网
    @Getter
    boolean isCtx;
    private ChatWindow currentWindow;
    public MainFrame(Integer userId) {
        setTitle("Main Frame");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                chatListController.closeCtx();
                super.windowClosing(e);
            }
        });
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
            Map<Object,ChatWindow>chatWindowMap=chatListPanel.getChatWindowMap();
            chatWindowMessageControllerMap=chatListPanel.getChatWindowMessageControllerMap();
            MessageCache.setChatWindowMessageControllerMap(chatWindowMessageControllerMap);
            MessageCache.setChatListController(chatListController);
            for(Map.Entry<Object,ChatWindow> entry:chatWindowMap.entrySet()){
                entry.getValue().setBounds(300, 0, 610, 613);
                add(entry.getValue());
            }
            revalidate();
            repaint();
        }).start();


    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainFrame(1);
            }
        });
    }

    public void addCtx(ChannelHandlerContext ctx){
        isCtx=true;
        chatListController.setCtx(ctx);
        ThreadPoolManager.getDBExecutorService().execute(() -> {
            try {
                chatListController.getLatch().await();
                for(Map.Entry<Object,ChatWindowMessageController> entry:chatWindowMessageControllerMap.entrySet()){
                    entry.getValue().setCtx(ctx);
                }
            }catch(InterruptedException e){}
        });
    }

    public void removeCtx(){
        isCtx=false;
        chatListController.setCtx(null);
        for(Map.Entry<Object,ChatWindowMessageController> entry:chatWindowMessageControllerMap.entrySet()){
            entry.getValue().setCtx(null);
        }
    }
}

