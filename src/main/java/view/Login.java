package view;

import entity.User;
import repository.UserRepository;
import service.MyBooksService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

    private final MyBooksService booksService;
    private JTextField tUsername;
    private JPasswordField passwordField;

    public Login() {
        booksService = new MyBooksService();
        prepareGUI();
    }

    private void prepareGUI() {
        this.setTitle("Login");

        Container contentPane = getContentPane();

        JLabel lHeader = new JLabel("My Books");
        lHeader.setHorizontalAlignment(SwingConstants.CENTER);
        lHeader.setPreferredSize(new Dimension(100, 20));
        lHeader.setFont(new Font("Arial", Font.BOLD, 14));

        contentPane.add(lHeader, BorderLayout.PAGE_START);

        JPanel loginData = new JPanel();
        loginData.setBorder(new EmptyBorder(10, 10, 10, 10));
        loginData.setLayout(new GridLayout(2, 2, 10, 5));

        JLabel lUsername = new JLabel("Username");
        lUsername.setPreferredSize(new Dimension(100, 20));

        tUsername = new JTextField();
        tUsername.setPreferredSize(new Dimension(200, 20));

        JLabel lPassword = new JLabel("Password");
        lPassword.setPreferredSize(new Dimension(100, 20));

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 20));

        loginData.add(lUsername);
        loginData.add(tUsername);
        loginData.add(lPassword);
        loginData.add(passwordField);

        contentPane.add(loginData, BorderLayout.CENTER);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.PAGE_AXIS));
        buttonPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton bLogin = new JButton("Login");
        bLogin.setPreferredSize(new Dimension(50, 20));
        bLogin.addActionListener(this::login);

        JButton bRegister = new JButton("Not yet an account?");
        bRegister.setPreferredSize(new Dimension(50, 10));
        bRegister.setBorderPainted(false);
        bRegister.setOpaque(false);
        bRegister.setBackground(Color.WHITE);
        bRegister.addActionListener((event) -> new RegisterDialog());

        buttonPane.add(bLogin);
        buttonPane.add(bRegister);

        contentPane.add(buttonPane, BorderLayout.PAGE_END);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.pack();
    }

    private void login(ActionEvent event) {
        User user = booksService.loginUser(tUsername.getText(), passwordField.getPassword());
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Username or password invalid", "Authentication failed", JOptionPane.ERROR_MESSAGE);
        } else {
            new MainView(user);
        }
    }
}
