import java.io.Serializable;

public class Message implements Serializable {
    private String sender;
    private String text;

    public Message(String sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public static Message fromString(String str) {
        String[] parts = str.split(":", 2);
        return new Message(parts[1], parts[0]);
    }

    @Override
    public String toString() {
        return sender + ": " + text;
    }
}
