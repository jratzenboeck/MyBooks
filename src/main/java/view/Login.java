package view;

import entity.User;
import service.MyBooksService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Login extends JFrame {

    private final MyBooksService booksService;
    private final JTextField tUsername;
    private final JPasswordField passwordField;

    public Login() {
        booksService = new MyBooksService();

        this.setTitle("Login");

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        JLabel lHeader = new JLabel("My Books");
        lHeader.setHorizontalAlignment(SwingConstants.CENTER);
        lHeader.setPreferredSize(new Dimension(100, 20));
        lHeader.setFont(new Font("Arial", Font.BOLD, 14));

        contentPane.add(lHeader, BorderLayout.PAGE_START);

        JPanel loginData = new JPanel();
        loginData.setBorder(new EmptyBorder(10, 10, 10, 10));
        loginData.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel lUsername = new JLabel("Username");
        lUsername.setPreferredSize(new Dimension(100, 20));

        tUsername = new JTextField();
        tUsername.setPreferredSize(new Dimension(200, 20));

        JLabel lPassword = new JLabel("Password");
        lPassword.setPreferredSize(new Dimension(100, 20));

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 20));

        JButton bLogin = new JButton("Login");
        bLogin.setPreferredSize(new Dimension(50, 20));
        bLogin.addActionListener((event) -> login());

        JButton bRegister = new JButton("Not yet an account?");
        bRegister.setPreferredSize(new Dimension(50, 10));
        bRegister.setBorderPainted(false);
        bRegister.setOpaque(false);
        bRegister.setBackground(Color.WHITE);
        bRegister.addActionListener((event) -> new RegisterDialog());

        loginData.add(lUsername);
        loginData.add(tUsername);
        loginData.add(lPassword);
        loginData.add(passwordField);
        loginData.add(bLogin);
        loginData.add(bRegister);

        contentPane.add(loginData, BorderLayout.CENTER);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.setVisible(true);
        this.pack();
    }

    private void login() {
        try {
            User user = booksService.loginUser(tUsername.getText(), passwordField.getPassword());
            new MainView(user);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Username or password invalid", "Authentication failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
