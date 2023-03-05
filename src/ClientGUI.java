import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientGUI {
    private JFrame frame;
    private JTextArea messageArea;
    private JTextField messageField;
    private JButton sendButton;
    private ChatClient client;

    public static void main(String[] args) {
        String username = JOptionPane.showInputDialog("Enter your username:");
        ClientGUI gui = new ClientGUI(username);
        gui.start();
    }

    public ClientGUI(String username) {
        client = new ChatClient(username);

        frame = new JFrame("Chat Client - " + username);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);

        messageArea = new JTextArea();
        messageArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(messageArea);

        messageField = new JTextField();

        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = messageField.getText();
                if (!message.isEmpty()) {
                    client.sendMessage(message);
                    messageField.setText("");
                }
            }
        });

        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        frame.add(bottomPanel, BorderLayout.SOUTH);
    }

    public void start() {
        frame.setVisible(true);
        client.connect();
        receiveMessages();
    }

    public void receiveMessages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Message message = client.receiveMessage();
                        messageArea.append(message.getSender() + ": " + message.getText() + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void stop() {
        client.disconnect();
        frame.dispose();
    }
}
