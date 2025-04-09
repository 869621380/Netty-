package org.example.Model.message;

// 图片消息内容类
public class SingleImageChatMessage extends ChatMessage {
    @Override
    public int getMessageType() {
        return SingleImageRequestMessage;
    }

//    @Override
//    public JPanel getContentPanel(boolean isSelf) {
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
//
//        try {
//            BufferedImage img = ImageIO.read(file);
//            Image scaledImage = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
//            JLabel fileOrImageLabel = new JLabel(new ImageIcon(scaledImage));
//            messageBubblePanel.add(fileOrImageLabel, BorderLayout.CENTER);
//
//            // 添加鼠标双击事件监听器
//            fileOrImageLabel.addMouseListener(new MouseAdapter() {
//                @Override
//                public void mouseClicked(MouseEvent e) {
//                    if (e.getClickCount() == 2) {
//                        previewImage(file);
//                    }
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return messageBubblePanel;
//    }
//
//    private void previewImage(File imageFile) {
//        try {
//            BufferedImage img = ImageIO.read(imageFile);
//            JFrame previewFrame = new JFrame("图片预览");
//            previewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//            JLabel imageLabel = new JLabel(new ImageIcon(img));
//            previewFrame.getContentPane().add(imageLabel, BorderLayout.CENTER);
//            previewFrame.pack();
//            previewFrame.setLocationRelativeTo(null);
//            previewFrame.setVisible(true);
//        } catch (IOException e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "预览图片时出错");
//        }
//    }
}