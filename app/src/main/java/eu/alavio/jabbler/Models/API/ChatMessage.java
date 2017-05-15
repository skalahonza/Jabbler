package eu.alavio.jabbler.Models.API;

import org.jivesoftware.smack.packet.Message;

/**
 * ChatMessage object displayed in chat
 */

public class ChatMessage {
    private boolean left;
    private String message;

    protected ChatMessage(Message message, boolean left) {
        this.left = left;
        this.message = message != null ? message.getBody() : "Empty message";
    }

    protected ChatMessage(String message, boolean left) {
        this.left = left;
        this.message = message != null ? message : "Empty message";
    }

    public static ChatMessage ReceivedMessage(Message message) {
        return new ChatMessage(message, false);
    }

    public static ChatMessage ReceivedMessage(String message) {
        return new ChatMessage(message, false);
    }

    public static ChatMessage ToBeSendMessage(Message message) {
        return new ChatMessage(message, true);
    }

    public static ChatMessage ToBeSendMessage(String message) {
        return new ChatMessage(message, true);
    }

    public boolean isLeft() {
        return left;
    }

    public String getMessage() {
        return message;
    }
}
