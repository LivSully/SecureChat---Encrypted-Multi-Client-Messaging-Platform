// Added a clear chat button.
// This program will make the client GUI with the title,
// logo, chat history, send button, and clear button.
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class CSC_Client_GUI_Draft {
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField inputField;
    private PrintWriter output;
    private Socket socket;

    public CSC_Client_GUI_Draft() {
        frame = new JFrame("Cool Secure Chat");
        chatArea = new JTextArea(20, 50);
        chatArea.setEditable(false);
        inputField = new JTextField(50);

        ImageIcon logo = new ImageIcon("CoolSecureChatLogo.png");
        Image img = logo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(img));
        lblLogo.setHorizontalAlignment(JLabel.CENTER);
        
        JButton sendButton = new JButton("Send");
        JButton clearButton = new JButton("Clear");

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(lblLogo, BorderLayout.NORTH);
        panel.add(inputField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.SOUTH);
        panel.add(clearButton, BorderLayout.EAST);

        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);


        inputField.addActionListener(e -> sendMessage());
        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> clear());
        clearButton.addActionListener(e -> clear());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        connectToServer();
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 5000);
            output = new PrintWriter(socket.getOutputStream(), true);

            new Thread(() -> {
                try {
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String message;
                    while ((message = input.readLine()) != null) {
                        chatArea.append(message + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            chatArea.append("Unable to connect to server.\n");
        }
    }

    private void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            output.println("Client: " + message);
            chatArea.append("You: " + message + "\n");
            inputField.setText("");
        }
    }
    private void clear() {
        chatArea.append("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CSC_Client_GUI_Draft::new);
    }
}
