/*




*/
import java.io.*;
import java.net.*;

public class CSC_Client_Draft {
    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int port = 5000;

        try {
            Socket socket = new Socket(serverAddress, port);
            System.out.println("Connected to server at " + serverAddress + ":" + port);

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter your username: ");
            String username = consoleInput.readLine();
            output.println(username);

            Thread listener = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = input.readLine()) != null) {
                        System.out.println("\n" + serverMessage);
                        System.out.print("You: ");
                    }
                } catch (IOException e) {
                    System.out.println("\nConnection closed.");
                }
            });

            listener.start();

            String message;
            while (true) {
                System.out.print("You: ");
                message = consoleInput.readLine();

                if (message == null) {
                    break;
                }

                output.println(message);

                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
            }

            socket.close();
        } catch (IOException e) {
            System.out.println("Unable to connect to server.");
            e.printStackTrace();
        }
    }
}