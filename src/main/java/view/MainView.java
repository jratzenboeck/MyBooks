package view;

import entity.User;

import javax.swing.*;

public class MainView extends JFrame {

    public MainView(User user) {
        this.setTitle("MyBooks - " + user.getUsername());
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.pack();
    }
}
