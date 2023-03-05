import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class ClientGUI extends JFrame implements ActionListener {

    private final ChatClient client;

    private JLabel label;
    private JTextField textField;
    private JButton button;
    private JTextArea chatArea;

    public ClientGUI(ChatClient client) {
        this.client = client;
        setGUI();
    }

    public void setGUI() {
        label = new JLabel("Enter your message:");
        textField = new JTextField(20);
        button = new JButton("Send");
        button.addActionListener(this);
        chatArea = new JTextArea(10, 20);
        chatArea.setEditable(false);

        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(textField);
        panel.add(button);

        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        chatScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        getContentPane().add(BorderLayout.CENTER, chatScrollPane);
        getContentPane().add(BorderLayout.SOUTH, panel);

        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ev) {
        String text = textField.getText();
        client.sendMessage(text);
        textField.setText("");
    }

    public void appendToChatArea(String message) {
        chatArea.append(message + "\n");
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        ClientGUI gui = new ClientGUI(client);
        client.setGui(gui);
        gui.setVisible(true);
        client.run();
    }
}


