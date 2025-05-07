package org.example.View;

import org.example.Controller.ChatListController;
import org.example.Model.Domain.ChatItem;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class test {

    public static void main(String[] args) {
        System.out.println(MessagePanel.class);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setBounds(100, 100, 1000, 650);
            //    ChatWindow chatWindow=new ChatWindow(1,2);
             //   frame.add(chatWindow);
                ChatListPanel chatListPannels = new ChatListPanel(1);
                ChatListController chatListController=new ChatListController(chatListPannels);
                frame.add(chatListPannels);
                frame.setVisible(true);
          //      ChatWindowMessageController chatWindowMessageController=new ChatWindowMessageController(chatWindow);
            }
        });
    }
}
