/*

*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CSC_Client_Draft {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private CHAT_GUI_TEST gui;
    // private final String IP_ADDRESS = "127.0.0.1";
    private static final int port = 5000;

    public CHAT_CLIENT_TEST(String host, int port, CHAT_GUI_TEST gui) throws IOException {
        this.gui = gui;
        socket = new Socket(host, port);

        out = new PrintWriter(socket.getOutputStream(), true);
        in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        new Thread(new ClientListener()).start();
    }

    public void sendMessage(String plaintext) {
        try {
            String encrypted = AESUtil.encrypt(plaintext);
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
        CHAT_GUI_TEST gui = new CHAT_GUI_TEST();
        try {
            // ip address goes where localhost is
            gui.connect("localhost", port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}