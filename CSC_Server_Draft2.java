import java.io.*;
import java.net.*;
import java.util.*;

public class CSC_Server_Draft2 {
    public static class CSC_Server {
        private ServerSocket serverSocket;
        private List<ClientHandler> clients = new ArrayList<>();
        private BufferedWriter logWriter;
        public void start(int port) throws IOException {
            serverSocket = new ServerSocket(port);
            logWriter = new BufferedWriter(new FileWriter("chatlog.txt", true));
            System.out.println("Server running...");
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket, this);
                clients.add(handler);
                new Thread(handler).start();
            }
        }
        public synchronized void broadcast(String encryptedMsg) {
            logMessage(encryptedMsg); // log before sending
            for (ClientHandler c : clients) {
                c.send(encryptedMsg);
            }
        }
        private synchronized void logMessage(String encryptedMsg) {
            try {
                logWriter.write(encryptedMsg);
                logWriter.newLine();
                logWriter.flush();
            } catch (IOException e) {
                System.out.println("Error writing to chatlog file");
            }
        }
        public synchronized void remove(ClientHandler handler) {
            clients.remove(handler);
        }
        public static void main(String[] args) {
            CSC_Server server = new CSC_Server();
            try {
                server.start(5000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static class ClientHandler implements Runnable {
        private Socket socket;
        private CSC_Server server;
        private PrintWriter out;
        private BufferedReader in;
        public ClientHandler(Socket socket, CSC_Server server) {
            this.socket = socket;
            this.server = server;
        }
        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String encryptedMsg;
                while ((encryptedMsg = in.readLine()) != null) {
                    server.broadcast(encryptedMsg);
                }
            } catch (IOException e) {
                System.out.println("Error handling disconnected client");
            } finally {
                server.remove(this);
                close();
            }
        }
        public void send(String encryptedMsg) {
            out.println(encryptedMsg);
        }
        private void close() {
            try { 
                socket.close(); 
            } catch (IOException ignored) {
                System.out.println("Error closing socket");
            }
        }
    }
}