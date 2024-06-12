package Minip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        setTitle("Login Page");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(255, 240, 240)); 

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2));
        loginPanel.setBackground(new Color(255, 240, 240)); 

        loginPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        loginPanel.add(usernameField);

        loginPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        loginPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginButtonListener());
        loginPanel.add(loginButton);

        add(loginPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);

            if ("admin".equals(username) && "admin".equals(password)) {
                openRecordManagementWindow();
                dispose(); 
            } else {
                JOptionPane.showMessageDialog(LoginPage.this, "Invalid username or password. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                
                usernameField.setText("");
                passwordField.setText("");
            }
        }
    }

    private void openRecordManagementWindow() {
        RecordManagementWindow recordManagementWindow = new RecordManagementWindow();
        recordManagementWindow.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }
}
