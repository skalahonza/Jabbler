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

    /**
     * Build new message that was received by ChatManager
     *
     * @param message Message received by the manager
     * @return New instance of ChatMessage, used in HistoryChatManager and ListViews
     */
    public static ChatMessage ReceivedMessage(Message message) {
        String partner_JID = message.getFrom();
        //split redundant info
        if (partner_JID.contains("/")) {
            partner_JID = partner_JID.split("/")[0];
        }
        return new ChatMessage(message, false, partner_JID);
    }

    /**
     * Build new message that is meant to be send by ChatManager from Message object
     * @param message Message object used for building
     * @return New instance of ChatMessage, used in HistoryChatManager and ListViews
     */
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
