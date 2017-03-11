package view;

import entity.User;
import service.MyBooksService;

import javax.swing.*;
import java.awt.*;

class RegisterDialog extends JDialog {

    private final MyBooksService bookService;
    private JTextField tUsername;
    private JPasswordField passwordField;
    private JList listReadingInterests;

    RegisterDialog() {
        this.bookService = new MyBooksService();

        this.setTitle("Register");
        this.setModal(true);

        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.weightx = 0.5;

        JLabel header = new JLabel("Register");
        header.setPreferredSize(new Dimension(100, 20));
        header.setFont(new Font("Arial", Font.BOLD, 14));
        constraints.gridx = 0;
        constraints.gridy = 0;
        contentPane.add(header, constraints);

        JLabel lUsername = new JLabel("Username");
        lUsername.setPreferredSize(new Dimension(100, 20));
        constraints.gridx = 0;
        constraints.gridy = 1;
        contentPane.add(lUsername, constraints);

        tUsername = new JTextField();
        tUsername.setPreferredSize(new Dimension(200, 20));
        constraints.gridx = 1;
        contentPane.add(tUsername, constraints);

        JLabel lPassword = new JLabel("Password");
        lPassword.setPreferredSize(new Dimension(100, 20));
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPane.add(lPassword, constraints);

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 20));
        constraints.gridx = 1;
        contentPane.add(passwordField, constraints);

        JLabel lReadingInterests = new JLabel("Your reading interests");
        lReadingInterests.setPreferredSize(new Dimension(200, 20));
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        contentPane.add(lReadingInterests, constraints);

        ReadingInterestListModel model = new ReadingInterestListModel(bookService.getCategories());
        listReadingInterests = new JList(model);
        listReadingInterests.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listReadingInterests.setLayoutOrientation(JList.VERTICAL);
        listReadingInterests.setVisibleRowCount(-1);

        JScrollPane listScroller = new JScrollPane(listReadingInterests);
        constraints.gridy = 4;
        constraints.ipady = 50;
        contentPane.add(listScroller, constraints);

        JButton bCancel = new JButton("Cancel");
        bCancel.setPreferredSize(new Dimension(100, 20));
        bCancel.addActionListener((event) -> this.setVisible(false));
        constraints.gridy = 5;
        constraints.gridwidth = 1;
        constraints.ipady = 0;
        contentPane.add(bCancel, constraints);

        JButton bSave = new JButton("Save");
        bSave.setPreferredSize(new Dimension(100, 20));
        bSave.addActionListener((e) -> registerUser());
        constraints.gridx = 1;
        constraints.gridwidth = 1;
        contentPane.add(bSave, constraints);

        this.pack();
        this.setVisible(true);
    }

    private void registerUser() {
        User registeredUser = bookService.
                registerUser(tUsername.getText(), passwordField.getPassword(),
                        listReadingInterests.getSelectedValuesList());
        if (registeredUser != null) {
            JOptionPane.showMessageDialog(this, "User " + registeredUser.getUsername() + " registered successfully.", "Registered", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "There was an error while registering user.", "Not Registered", JOptionPane.ERROR_MESSAGE);
        }
        this.setVisible(false);
    }
}
