
/*

This ONE


*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CSC_Client_Draft2 {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ChatClientGUI gui;
    private String username;
    private static final String IP_ADDRESS = "127.0.0.1";
    private static final int port = 1111;

    public CSC_Client_Draft2(String host, int port, ChatClientGUI gui, String username) throws IOException {
        this.gui = gui;
        this.username = username;
        socket = new Socket(host, port);

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        new Thread(new ClientListener()).start();
    }

    public void sendMessage(String plaintext) {
        try {
            String fullMessage = username + ": " + plaintext;
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
            } catch (IOException e) {
                gui.appendMessage("Disconnected from server.");
            }
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