package org.example.Model.message;

// 文本消息内容类
public class SingleTextChatMessage extends SingleChatChatMessage {
    @Override
    public int getMessageType() {
        return SingleTextRequestMessage;
    }
//    @Override
//    public JPanel getContentPanel(boolean isSelf) {
//        return getjPanel(isSelf, content);
//    }
//
//    public static JPanel getjPanel(boolean isSelf, String content) {
//        JTextArea messageTextArea = new JTextArea(content);
//        messageTextArea.setEditable(false);
//        messageTextArea.setLineWrap(true);
//        messageTextArea.setWrapStyleWord(true);
//        messageTextArea.setOpaque(false);
//        messageTextArea.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
//
//        JPanel messageBubblePanel = new JPanel(new BorderLayout());
//        messageBubblePanel.setOpaque(true);
//        if (isSelf) {
//            messageBubblePanel.setBackground(new Color(200, 255, 200));
//            messageBubblePanel.setBorder(BorderFactory.createCompoundBorder(
//                    BorderFactory.createLineBorder(new Color(150, 255, 150)),
//                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
//            ));
//        } else {
//            messageBubblePanel.setBackground(Color.WHITE);
//            messageBubblePanel.setBorder(BorderFactory.createCompoundBorder(
//                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
//                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
//            ));
//        }
//        messageBubblePanel.add(messageTextArea, BorderLayout.CENTER);
//
//        return messageBubblePanel;
//    }


}
