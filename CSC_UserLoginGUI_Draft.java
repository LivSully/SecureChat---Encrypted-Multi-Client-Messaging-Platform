/* 
Data Storage:
    Credentials are stored in credentials.txt in the format:
    user1,password1
    user2,password2
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

public class CSC_UserLoginGUI_Draft extends JFrame implements ActionListener {
    // Components for Login Forms
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;

    public CSC_UserLoginGUI_Draft() {
        setTitle("Sign In to Cool Secure Chat!");
        setSize(450, 300);
        setLayout(new BorderLayout(10, 10)); // Using BorderLayout for better structure

        // Creating Panels
        JPanel mainPanel = new JPanel(new GridBagLayout()); // For Form Layout
        JPanel buttonPanel = new JPanel(new FlowLayout()); // For Buttons

        // Constraints for GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        ImageIcon logo = new ImageIcon("CoolSecureChatLogo.png");
        Image img = logo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(img));
        lblLogo.setHorizontalAlignment(JLabel.CENTER);

        add(lblLogo, BorderLayout.NORTH);

        // Username Label & TextField
        JLabel lblUser = new JLabel("Username:");
        txtUser = new JTextField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(lblUser, gbc);
        gbc.gridx = 1;
        mainPanel.add(txtUser, gbc);

        // Password Label & PasswordField (Masked)
        JLabel lblPass = new JLabel("Password:");
        txtPass = new JPasswordField(15);
        txtPass.setEchoChar('*'); // Mask password input

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(lblPass, gbc);
        gbc.gridx = 1;
        mainPanel.add(txtPass, gbc);

        // Button for Login
        btnLogin = new JButton("Login");

        btnLogin.addActionListener(this);

        buttonPanel.add(btnLogin);

        // Adding panels to Frame
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center window
        setVisible(true);
    }

    // Handling Button Clicks
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            loginUser();
        }
    }

    // Method to Login User
    private void loginUser() {
        String username = txtUser.getText();
        String password = new String(txtPass.getPassword());
        boolean success = false;

        try (Scanner sc = new Scanner(new File("credentials.txt"))) {
            while (sc.hasNextLine()) {
                String[] credentials = sc.nextLine().split(",");
                if (credentials[0].equals(username) && credentials[1].equals(password)) {
                    success = true;
                    break;
                }
            }
        } catch (Exception ex) {
            showMessage("Error", "User data file not found!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (success) {
            showMessage("Login Successful", "Welcome, " + username + "!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            showMessage("Login Failed", "Invalid username or password.", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to Show Message Dialog
    private void showMessage(String title, String message, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    public static void main(String[] args) {
        new CSC_UserLoginGUI_Draft();
    }
}
