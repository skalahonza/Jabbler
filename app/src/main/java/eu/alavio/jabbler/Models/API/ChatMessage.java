package eu.alavio.jabbler.Models.API;

import org.jivesoftware.smack.packet.Message;

import java.util.Date;

/**
 * ChatMessage object displayed in chat
 */

public class ChatMessage {
    private boolean received;
    private Message message;
    private Date timestamp;
    private String partner_JID;

    protected ChatMessage(Message message, boolean received, String partner_jid) {
        this.received = received;
        this.message = message;
        partner_JID = partner_jid;
        timestamp = new Date();
    }

    public static ChatMessage ReceivedMessage(Message message) {
        String partner_JID = message.getFrom();
        //split redundant info
        if (partner_JID.contains("/")) {
            partner_JID = partner_JID.split("/")[0];
        }
        return new ChatMessage(message, false, partner_JID);
    }

    public static ChatMessage ToBeSendMessage(Message message) {
        String partner_JID = message.getTo();
        return new ChatMessage(message, true, partner_JID);
    }

    public boolean isReceived() {
        return received;
    }

    public Message getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getPartner_JID() {
        return partner_JID;
    }
}
