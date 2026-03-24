
/*

This ONE


*/
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.SwingUtilities;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ChatClientGUI gui;
    private String username;
    private static final String IP_ADDRESS = "10.1.37.88";
    private static final int port = 1111;

    public Client(String host, int port, ChatClientGUI gui, String username) throws IOException {
        this.gui = gui;
        this.username = username;
        socket = new Socket(host, port);

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        new Thread(new ClientListener()).start();
    }

    public class MessageFactory {

        public static String createFormattedMessage(String username, String message) {
            return username + ": " + message;
        }
    }

    public void sendMessage(String plaintext) {
        try {
            String fullMessage = MessageFactory.createFormattedMessage(username, plaintext);
            String encrypted = AESUtil.encrypt(fullMessage);
            out.println(encrypted);
        } catch (Exception e) {
            gui.appendMessage("Encryption error");
        }
    }

    private class ClientListener implements Runnable {
        public void run() {
            try {
                String encryptedMsg;
                while ((encryptedMsg = in.readLine()) != null) {
                    try {
                        String decrypted = AESUtil.decrypt(encryptedMsg);
                        gui.appendMessage(decrypted);
                    } catch (Exception e) {
                        gui.appendMessage("[Error decrypting message]");
                    }
                }
                SwingUtilities.invokeLater(() -> {
                    gui.getStatusIndicator().setBackground(Color.RED);
                });
            } catch (IOException e) {
                gui.appendMessage("Disconnected from server.");
            }
        }
    }

    public void disconnect() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ChatClientGUI gui = new ChatClientGUI("User");
        try {
            // ip address goes where localhost is
            gui.connect(IP_ADDRESS, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}