package eu.alavio.jabbler.Models.API;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import eu.alavio.jabbler.Models.Helpers.DatabaseHelper;

/**
 * Manager for manipulating with chat history
 */

public class ChatHistoryManager {
    private Context context;
    private DatabaseHelper db;

    public ChatHistoryManager(Context context) {
        this.context = context;
        try {
            db = new DatabaseHelper(context, ApiHandler.getCurrentUser().getJid());
        } catch (SmackException.NotConnectedException | XMPPException.XMPPErrorException | SmackException.NoResponseException e) {
            Log.e(ChatHistoryManager.class.getName(), "init DB connection:", e);
        }
    }

    /**
     * Save message into database
     *
     * @param message Received or sent message
     * @return Tre if successful
     */
    public boolean saveMessage(ChatMessage message) {
        try {
            return db.addData(message);
        } catch (Exception e) {
            Log.e(ChatHistoryManager.class.getName(), "save message: " + message.toString(), e);
            return false;
        }
    }

    /**
     * Retrieve communication between current user and given contact
     *
     * @param contact Chat partner
     * @return null, if error
     */
    public List<ChatMessage> getMessagesWith(Friend contact) {
        return getMessagesWith(contact.getJid());
    }

    /**
     * Retrieve communication between current user and given JID, used for anonymous chats and deleted contacts
     *
     * @param jid Chat partner JID
     * @return null, if error
     */
    public List<ChatMessage> getMessagesWith(String jid) {
        try {
            Cursor data = db.getMessagesFrom(jid, ApiHandler.getCurrentUser().getJid());
            Gson gson = new Gson();
            List<ChatMessage> messages = new ArrayList<>();

            while (data.moveToNext()) {
                String json = data.getString(data.getColumnIndex(DatabaseHelper.MESSAGE));
                messages.add(gson.fromJson(json, ChatMessage.class));
            }
            return messages;
        } catch (SmackException.NotConnectedException | XMPPException.XMPPErrorException | SmackException.NoResponseException e) {
            Log.e(ChatHistoryManager.class.getName(), "get messages", e);
            return null;
        }
    }

    /**
     * Get unique days from message history
     *
     * @return null on error
     */
    public List<Date> getDays() {
        Cursor data = db.getDays();
        List<Date> dates = new ArrayList<>();
        while (data.moveToNext()) {
            String date = data.getString(data.getColumnIndex(DatabaseHelper.DATE));
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd", Locale.getDefault());
            try {
                dates.add(format.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        return dates;
    }

    /**
     * Get messages that happened in a given day
     *
     * @param day Day used for filter
     * @return Empty list if no message happened that day
     */
    public List<ChatMessage> getMessagesFromDay(Date day) {
        Cursor data = db.getMessagesFromDay(day);
        Gson gson = new Gson();
        List<ChatMessage> messages = new ArrayList<>();

        while (data.moveToNext()) {
            String json = data.getString(data.getColumnIndex(DatabaseHelper.MESSAGE));
            messages.add(gson.fromJson(json, ChatMessage.class));
        }
        return messages;
    }


    /**
     * Get latest messages, reduce the amount of items by count parameter
     *
     * @param count Maximum amount of returned messages
     * @return List that will have <= count of objects
     */
    public List<ChatMessage> getLatestMessages(int count) {
        Cursor data = db.getLatestMessages(count);
        Gson gson = new Gson();
        List<ChatMessage> messages = new ArrayList<>();

        while (data.moveToNext()) {
            String json = data.getString(data.getColumnIndex(DatabaseHelper.MESSAGE));
            messages.add(gson.fromJson(json, ChatMessage.class));
        }
        messages.sort((o1, o2) -> -1 * o1.getTimestamp().compareTo(o2.getTimestamp()));
        return messages;
    }
}
