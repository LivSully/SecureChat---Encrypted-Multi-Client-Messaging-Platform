package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private Server server;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String encryptedMsg;
            while ((encryptedMsg = in.readLine()) != null) {
                server.broadcast(encryptedMsg, this);
            }
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            server.removeClient(this);
            close();
        }
    }

    public void send(String encryptedMsg) {
        if (out != null) {
            out.println(encryptedMsg);
        }
    }

    private void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing client socket.");
        }
    }
}
