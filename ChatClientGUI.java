
//this one
import java.io.IOException;

import javax.swing.*;
import java.awt.*;

public class ChatClientGUI extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private CHAT_CLIENT_TEST client;
    private JButton sendButton;
    private JButton clearButton;

    public ChatClientGUI() {

        ImageIcon logo = new ImageIcon("CoolSecureChatLogo.png");
        // Image img = logo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(logo);
        lblLogo.setHorizontalAlignment(JLabel.CENTER);
        //add(lblLogo, BorderLayout.NORTH);

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
        client = new CHAT_CLIENT_TEST(host, port, this);
    }

    public void appendMessage(String msg) {
        chatArea.append(msg + "\n");
    }

}
