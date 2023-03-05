import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();

    public static void main(String[] args) {
        new ChatServer().start();
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Chat server started on port 1234...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");
                ClientHandler client = new ClientHandler(clientSocket);
                clients.add(client);
                new Thread(client).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void broadcast(Message message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    private class ClientHandler implements Runnable {
        private BufferedReader reader;
        private PrintWriter writer;
        private Socket socket;
        private String username;

        public ClientHandler(Socket clientSocket) {
            try {
                socket = clientSocket;
                InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
                reader = new BufferedReader(streamReader);
                writer = new PrintWriter(socket.getOutputStream());
                System.out.println("New client handler created");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            String messageString;
            try {
                username = reader.readLine();
                System.out.println("Username set to: " + username);
                while ((messageString = reader.readLine()) != null) {
                    Message message = Message.fromString(messageString);
                    System.out.println("Received message from " + username + ": " + message.getText());
                    broadcast(message);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        private void sendMessage(Message message) {
            writer.println(message.toString());
            writer.flush();
        }
    }
}
