package eu.alavio.jabbler.Models.API;

import org.jivesoftware.smack.packet.Message;

import java.util.Date;

/**
 * ChatMessage object displayed in chat
 */

public class ChatMessage {
    private boolean left;
    private Message message;
    private Date timestamp;

    protected ChatMessage(Message message, boolean left) {
        this.left = left;
        this.message = message;
        timestamp = new Date();
    }

    public static ChatMessage ReceivedMessage(Message message) {
        return new ChatMessage(message, false);
    }

    public static ChatMessage ToBeSendMessage(Message message) {
        return new ChatMessage(message, true);
    }

    public boolean isLeft() {
        return left;
    }

    public Message getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
