import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    private String username;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ChatClient(String username) {
        this.username = username;
    }

    public void connect() {
        try {
            socket = new Socket("localhost", 1234);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(new Message(username, message));
    }

    public Message receiveMessage() throws IOException {
        return Message.fromString(in.readLine());
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
