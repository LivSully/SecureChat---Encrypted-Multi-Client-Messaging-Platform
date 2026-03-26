import java.io.IOException;

import javax.swing.*;
import java.awt.*;

public class ChatClientGUI extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private Client client;
    private String username;
    private JButton sendButton;
    private JButton clearButton;
    private JButton logoutButton;
    private JLabel statusIndicator;
    private ChatController controller;

    public ChatClientGUI(String username) {
        this.username = username;

        setTitle("Cool Secure Chat - " + username);
        setLayout(new BorderLayout());

        // Logo
        ImageIcon logo = new ImageIcon(getClass().getResource("/CoolSecureChatLogo.png"));
        Image img = logo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(img));
        lblLogo.setHorizontalAlignment(JLabel.CENTER);
        add(lblLogo, BorderLayout.NORTH);

        // Chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        // Input field and buttons
        inputField = new JTextField();
        sendButton = new JButton("Send");
        clearButton = new JButton("Clear");
        logoutButton = new JButton("Logout");

        // Status indicator (green = connected, red = disconnected)
        statusIndicator = new JLabel();
        statusIndicator.setOpaque(true);
        statusIndicator.setBackground(Color.RED);
        statusIndicator.setPreferredSize(new Dimension(15, 15));
        statusIndicator.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Send on Enter key in input field
        inputField.addActionListener(e -> sendMessage());
        sendButton.addActionListener(e -> sendMessage());

        clearButton.addActionListener(e -> chatArea.setText(""));

        logoutButton.addActionListener(e -> {
            if (controller != null) {
                controller.disconnect();
            }
            statusIndicator.setBackground(Color.RED);
            appendMessage("You have logged out.");
            // Disable input after logout
            inputField.setEnabled(false);
            sendButton.setEnabled(false);
        });

        JPanel statusPanel = new JPanel();
        statusPanel.add(statusIndicator);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(sendButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(logoutButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(statusPanel, BorderLayout.WEST);
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // NOTE: setVisible(true) is intentionally NOT called here.
        // It is called by UserLoginGUI only after a successful connection.
    }

    private void sendMessage() {
        if (controller == null)
            return;
        String msg = inputField.getText().trim();
        if (!msg.isEmpty()) {
            controller.sendMessage(msg);
            inputField.setText("");
        }
    }

    public void connect(String host, int port) throws IOException {
        client = new Client(host, port, this, username);
        controller = new ChatController(client);
        statusIndicator.setBackground(Color.GREEN);
    }

    public void appendMessage(String msg) {
        SwingUtilities.invokeLater(() -> chatArea.append(msg + "\n"));
    }

    public JLabel getStatusIndicator() {
        return statusIndicator;
    }

    public class ChatController {
        private Client client;

        public ChatController(Client client) {
            this.client = client;
        }

        public void sendMessage(String message) {
            client.sendMessage(message);
        }

        public void disconnect() {
            client.disconnect();
        }
    }
}
