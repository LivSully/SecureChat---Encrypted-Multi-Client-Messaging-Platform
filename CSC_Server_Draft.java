import java.io.*;
import java.net.*;
import java.util.*;

public class CSC_Server_Draft {
    class CSCDraft {
        public static void main(String[] args) {
            int port = 5000;
            int maxClients = 10;
            int clientCount = 0;
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Server started. Waiting for clients...");
                // Accept incoming client connections
                while (clientCount < maxClients) {
                    Socket socket = serverSocket.accept();
                    clientCount++;
                    System.out.println("Client connected: " + socket.getInetAddress());
                    // Create a new thread for each client
                    // need to specify which clienthandler class goes here, depending on issues described below
                    //new ClientHandler1(socket, clientCount).start();
                }
                System.out.println("Max clients reached. No longer accepting connections");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // will be in separate file later

    class ClientHandler1 extends Thread {
        private Socket socket;
        private int clientNumber;
        public ClientHandler1(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
        }
        // what if run() returned a string of the message that was sent by the client, and then in the main method, there was a for loop that took that string and sent it to all of the clients? would that work?
        public void run() {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
                String message;
                while ((message = input.readLine()) != null) {
                    if (message.equalsIgnoreCase("exit")) {
                        System.out.println("Client " + clientNumber + "disconnected from the chatroom.");
                        break;
                    }
                }
                // THIS NEEDS TO BE SENT TO ALL OF THE CLIENTS/USERS
                System.out.println("User " + clientNumber + ": " + message);


            } catch (IOException e) {
                System.out.println("Error with client number " + clientNumber);
                e.printStackTrace();    
            }
        }
    }
    // i had the idea to try having the client handler take in all of the clients, but then how would it know which client is sending a message?
    // for clienthandler1 to work, there would have to be a way for the message that is being received to be send to all of the other clients in a for loop or something

    class ClientHandler2 extends Thread {
    
    
    }
}
