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
        db = new DatabaseHelper(context);
    }

    /**
     * Save message into database
     *
     * @param message Received or sent message
     * @return Tre if successful
     */
    public boolean saveMessage(ChatMessage message) {
        try {
            return db.addData(message, ApiHandler.getCurrentUser().getJid());
        } catch (SmackException.NotConnectedException | XMPPException.XMPPErrorException | SmackException.NoResponseException e) {
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
    public List<ChatMessage> getMessagesFrom(Friend contact) {
        try {
            Cursor data = db.getMessagesFrom(contact.getJid(), ApiHandler.getCurrentUser().getJid());
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
                    "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            try {
                dates.add(format.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        return dates;
    }

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
}
