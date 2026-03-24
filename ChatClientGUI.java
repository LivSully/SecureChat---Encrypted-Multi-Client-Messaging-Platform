
//this one
import java.io.IOException;

import javax.swing.*;
import java.awt.*;

public class ChatClientGUI extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private CSC_Client_Draft2 client;
    private String username;
    private JButton sendButton;
    private JButton clearButton;

    public ChatClientGUI(String username) {
        this.username = username;
        ImageIcon logo = new ImageIcon("CoolSecureChatLogo.png");
        // Image img = logo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(logo);
        lblLogo.setHorizontalAlignment(JLabel.CENTER);
        // add(lblLogo, BorderLayout.NORTH);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        inputField = new JTextField();
        sendButton = new JButton("Send");
        clearButton = new JButton("Clear");
        inputField.addActionListener(e -> {
            String msg = inputField.getText();
            client.sendMessage(msg); // plaintext → encrypted inside client
            inputField.setText("");
        });
        sendButton.addActionListener(e -> {
            String msg = inputField.getText();
            client.sendMessage(msg);
            inputField.setText("");
        });
        clearButton.addActionListener(e -> {
            chatArea.setText("");
        });
        add(new JScrollPane(chatArea), BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        // JPanel topPanel = new JPanel(new BorderLayout());
        buttonPanel.add(sendButton);
        buttonPanel.add(clearButton);
        // topPanel.add(lblLogo);
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
        setSize(400, 500);
        setVisible(true);
    }

    public void connect(String host, int port) throws IOException {
        client = new CSC_Client_Draft2(host, port, this, username);
    }

    public void appendMessage(String msg) {
        SwingUtilities.invokeLater(() -> chatArea.append(msg + "\n"));
    }

}
