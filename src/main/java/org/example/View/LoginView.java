package org.example.View;



import org.example.Controller.LoginController;
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


public class LoginView extends JFrame {

    private static final Logger log = LoggerFactory.getLogger(LoginView.class);
    private BufferedImage backgroundImage;
    private final JPanel backgroundPanel;

    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JTextField emailField;
    private final JTextField codeField;
    private final JButton loginButton;
    private final JButton getCodeButton;
    private final JButton registerButton;
    private final JButton backButton;

    private LoginMessageListener listener;

    boolean registerStatus = false;


    public LoginView() {
        setTitle("登录界面");
        setSize(330, 450);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        try {
            backgroundImage = ImageIO.read(new File("img.png"));
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        backgroundPanel = getJPanel();
        add(backgroundPanel);

        usernameField = new PlaceholderTextField("请输入账号");
        passwordField = new PlaceholderPasswordField();
        emailField = new PlaceholderTextField("请输入邮箱");
        codeField = new PlaceholderTextField("请输入验证码");


        loginButton = new JButton("登录");
        registerButton = new JButton("注册");
        getCodeButton=new JButton("获取验证码");
        backButton = new JButton("返回");
        backButton.addActionListener(new BackButtonClickListener());

        addListener();

        setLayout();
        addComponents();

        setVisible(true);
    }

    private void addListener() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null) {
                    if(!usernameField.getText().matches("^\\d+$")){
                        showMessage("请输入正确的账号格式");
                    }
                    else listener.onLoginRequest(usernameField.getText(), new String(passwordField.getPassword()));
                }
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null) {
                    if (!registerStatus) {
                        backgroundPanel.removeAll();
                        usernameField.setText("");
                        passwordField.setText("");
                        emailField.setText("");
                        codeField.setText("");
                        registerStatus = true;
                        setLayout();
                        addComponents();
                        repaint();
                    }
                    else listener.onRegisterRequest(emailField.getText(),codeField.getText(),new String(passwordField.getPassword()));
                }
            }
        });
        getCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null) {
                    listener.onGetCodeRequest(emailField.getText());
                }
            }
        });

    }

    public void setLoginMessageListener(LoginMessageListener listener) {
        this.listener = listener;
    }

    private JPanel getJPanel() {
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
                    g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(null);
        backgroundPanel.setBounds(0, 0, getWidth(), getHeight());
        return backgroundPanel;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }



    private class BackButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            backgroundPanel.removeAll();
            registerStatus = false;
            setLayout();
            passwordField.setText("");
            addComponents();
            repaint();
        }
    }

    private static class PlaceholderTextField extends JTextField {
        private final String placeholder;

        public PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
            setHorizontalAlignment(JTextField.CENTER);
        }

        private boolean isPlaceholderVisible() {
            return getText().isEmpty();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (isPlaceholderVisible()) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(Color.GRAY);
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(placeholder);
                int x = (getWidth() - textWidth) / 2;
                int y = fm.getAscent() + (getHeight() - fm.getHeight()) / 2;
                g2d.drawString(placeholder, x, y);
                g2d.dispose();
            }
        }
    }

    private static class PlaceholderPasswordField extends JPasswordField {
        private boolean isPlaceholderVisible() {
            return getPassword().length == 0;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (isPlaceholderVisible()) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(Color.GRAY);
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth("请输入密码");
                int x = (getWidth() - textWidth) / 2;
                int y = fm.getAscent() + (getHeight() - fm.getHeight()) / 2;

                g2d.drawString("请输入密码", x, y);
                setHorizontalAlignment(JPasswordField.CENTER);
                g2d.dispose();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoginView loginView = new LoginView();
                LoginController loginController = new LoginController(loginView);
            }
        });
    }


    private void setLayout() {
        if(!registerStatus) {
            usernameField.setBounds(50, 100, 200, 30);
            passwordField.setBounds(50, 150, 200, 30);
            loginButton.setBounds(50, 200, 200, 30);
            registerButton.setBounds(50, 250, 200, 30);
        }
        else {
            emailField.setBounds(50, 100, 200, 30);
            getCodeButton.setBounds(50, 140, 200, 30);
            codeField.setBounds(50, 180, 200, 30);
            passwordField.setBounds(50, 220, 200, 30);
            registerButton.setBounds(50, 260, 200, 30);
            backButton.setBounds(50, 300, 200, 30);
        }
    }

    /*
    登录注册页面切换
     */
    private void addComponents() {
        if(!registerStatus) {
            backgroundPanel.add(usernameField);
            backgroundPanel.add(passwordField);
            backgroundPanel.add(loginButton);
            backgroundPanel.add(registerButton);
        }
        else {
            backgroundPanel.add(passwordField);
            backgroundPanel.add(emailField);
            backgroundPanel.add(codeField);
            backgroundPanel.add(getCodeButton);
            backgroundPanel.add(registerButton);
            backgroundPanel.add(backButton);
        }
    }

    public void disableGetCodeButtonFor30Seconds() {
        getCodeButton.setEnabled(false);
        Timer timer = new Timer(30000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getCodeButton.setEnabled(true);
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }


    // 事件监听器接口
    public interface LoginMessageListener {
        void onLoginRequest(String username, String password);
        void onRegisterRequest(String email, String code,String password);
        void onGetCodeRequest(String email);
    }
}    