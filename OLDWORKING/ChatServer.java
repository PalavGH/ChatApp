import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private ArrayList<PrintWriter> clientOutputStreams;

    public static void main(String[] args) {
        new ChatServer().start();
    }

    public void start() {
        clientOutputStreams = new ArrayList<>();
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clientOutputStreams.add(writer);

                Thread thread = new Thread(new ClientHandler(clientSocket));
                thread.start();
                System.out.println("Got a connection");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public class ClientHandler implements Runnable {
        BufferedReader reader;
        Socket socket;

        public ClientHandler(Socket clientSocket) {
            try {
                socket = clientSocket;
                InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
                reader = new BufferedReader(isReader);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println("Received message: " + message);
                    sendToAllClients(message);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void sendToAllClients(String message) {
        Iterator<PrintWriter> it = clientOutputStreams.iterator();
        while (it.hasNext()) {
            try {
                PrintWriter writer = it.next();
                writer.println(message);
                writer.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
