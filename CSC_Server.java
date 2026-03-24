import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class CSC_Server {
    private ServerSocket serverSocket;
    private final List<ClientHandler> clients = new ArrayList<>();
    private BufferedWriter logWriter;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        logWriter = new BufferedWriter(new FileWriter("chatlog.txt", true));

        System.out.println("Server running on port " + port + "...");

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("New client connected: " + socket.getInetAddress());

            ClientHandler handler = new ClientHandler(socket, this);

            synchronized (clients) {
                clients.add(handler);
            }

            Thread clientThread = new Thread(handler);
            clientThread.start();
        }
    }

    public void broadcast(String encryptedMsg, ClientHandler sender) {
        logMessage(encryptedMsg);

        synchronized (clients) {
            for (ClientHandler client : clients) {
                client.send(encryptedMsg);
            }
        }
    }

    private void logMessage(String encryptedMsg) {
        try {
            logWriter.write(encryptedMsg);
            logWriter.newLine();
            logWriter.flush();
        } catch (IOException e) {
            System.out.println("Error writing to chatlog.txt");
        }
    }

    public void removeClient(ClientHandler handler) {
        synchronized (clients) {
            clients.remove(handler);
        }
        System.out.println("Client disconnected.");
    }

    public static void main(String[] args) {
        CSC_Server server = new CSC_Server();

        try {
            server.start(1111);
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
