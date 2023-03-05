import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient implements Runnable {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private ClientGUI gui;

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        ClientGUI gui = new ClientGUI(client);
        client.setGui(gui);
        Thread clientThread = new Thread(client);
        clientThread.start();
    }

    public void setGui(ClientGUI gui) {
        this.gui = gui;
    }

    public void sendMessage(String message) {
        writer.println(message);
        writer.flush();
    }

    public void run() {
        try {
            socket = new Socket("localhost", 1234);
            InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(socket.getOutputStream());
            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("read " + message);
                gui.appendToChatArea(message);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}