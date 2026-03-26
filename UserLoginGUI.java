import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

public class UserLoginGUI extends JFrame implements ActionListener {
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;
    private JLabel lblUserStatus, lblPassStatus;

    public UserLoginGUI() {
        setTitle("Sign In to Cool Secure Chat!");
        setSize(475, 350);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new GridBagLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 20, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Logo - loaded from classpath so it works from any working directory
        ImageIcon logo = new ImageIcon(getClass().getResource("/CoolSecureChatLogo.png"));
        Image img = logo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(img));
        lblLogo.setHorizontalAlignment(JLabel.CENTER);
        add(lblLogo, BorderLayout.NORTH);

        // Username
        JLabel lblUser = new JLabel("Username:");
        txtUser = new JTextField(25);

        // Status labels
        lblUserStatus = new JLabel(" ");
        lblPassStatus = new JLabel(" ");
        Dimension statusSize = new Dimension(320, 20);
        lblUserStatus.setPreferredSize(statusSize);
        lblPassStatus.setPreferredSize(statusSize);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        mainPanel.add(lblUserStatus, gbc);
        gbc.gridy = 3;
        mainPanel.add(lblPassStatus, gbc);

        txtUser.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validateUsername();
            }

            public void removeUpdate(DocumentEvent e) {
                validateUsername();
            }

            public void changedUpdate(DocumentEvent e) {
                validateUsername();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(lblUser, gbc);
        gbc.gridx = 1;
        mainPanel.add(txtUser, gbc);

        // Password
        JLabel lblPass = new JLabel("Password:");
        txtPass = new JPasswordField(25);
        txtPass.setEchoChar('*');

        txtPass.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validatePassword();
            }

            public void removeUpdate(DocumentEvent e) {
                validatePassword();
            }

            public void changedUpdate(DocumentEvent e) {
                validatePassword();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(lblPass, gbc);
        gbc.gridx = 1;
        mainPanel.add(txtPass, gbc);

        // Login button — also triggered by pressing Enter in the password field
        btnLogin = new JButton("Login");
        btnLogin.addActionListener(this);
        txtPass.addActionListener(this); // pressing Enter in password field clicks Login
        buttonPanel.add(btnLogin);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        loginUser();
    }

    private void validateUsername() {
        String username = txtUser.getText();
        if (username.matches("^\\w{4,16}$")) {
            lblUserStatus.setText("Valid username");
            lblUserStatus.setForeground(Color.GREEN.darker());
        } else {
            lblUserStatus.setText("User: 4-16 characters (a-Z, 0-9, or _)");
            lblUserStatus.setForeground(Color.RED);
        }
    }

    private void validatePassword() {
        String password = new String(txtPass.getPassword());
        if (password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$")) {
            lblPassStatus.setText("Valid password");
            lblPassStatus.setForeground(Color.GREEN.darker());
        } else {
            lblPassStatus.setText("Pass: 8-20 characters (a-Z, 0-9, and @$!%*?&)");
            lblPassStatus.setForeground(Color.RED);
        }
    }

    private void loginUser() {
        String username = txtUser.getText();
        String password = new String(txtPass.getPassword());

        if (!username.matches("^\\w{4,16}$")) {
            showMessage("Error", "Invalid username format.", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$")) {
            showMessage("Error", "Invalid password format.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check credentials
        boolean success = false;
        try (Scanner sc = new Scanner(new File("credentials.txt"))) {
            while (sc.hasNextLine()) {
                String[] credentials = sc.nextLine().split(",");
                if (credentials.length >= 2 &&
                        credentials[0].equals(username) &&
                        credentials[1].equals(password)) {
                    success = true;
                    break;
                }
            }
        } catch (Exception ex) {
            showMessage("Error", "User data file not found!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!success) {
            showMessage("Login Failed", "Invalid username or password.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Credentials valid — now attempt server connection BEFORE showing chat window
        try {
            ChatClientGUI chatGUI = new ChatClientGUI(username);
            chatGUI.connect("10.1.37.88", 1111); // throws IOException if server is unreachable
            // Connection succeeded: show chat window and close login window
            chatGUI.setVisible(true);
            dispose();
        } catch (IOException e) {
            // Connection failed: chat window was never shown, login window stays open
            showMessage("Error", "Could not connect to chat server.\nMake sure the server is running.",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showMessage(String title, String message, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UserLoginGUI::new);
    }
}
