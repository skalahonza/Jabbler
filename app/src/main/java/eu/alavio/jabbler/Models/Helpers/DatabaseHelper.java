package eu.alavio.jabbler.Models.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import eu.alavio.jabbler.Models.API.ChatMessage;

/**
 * Database helper fro communication with sql database
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    public static final String TABLE_NAME = "messages_table";
    public static final String ID = "ID"; //ID
    public static final String HOST = "host"; //current logged in user JID
    public static final String PARTNER = "partner"; //partner jid
    public static final String DATE = "date"; // automatic filed, day of insert
    public static final String MESSAGE = "message"; //json of message object

    private String host;


    public DatabaseHelper(Context context, String host) {
        super(context, TABLE_NAME, null, 1);
        this.host = host;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                HOST + " TEXT, " +
                PARTNER + " TEXT, " +
                DATE + " DATETIME DEFAULT CURRENT_DATE, " +
                MESSAGE + " TEXT" +
                ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Add new message to database
     *
     * @param message Received or sent message
     * @return True if successfully added
     */
    public boolean addData(ChatMessage message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(HOST, host);
        contentValues.put(PARTNER, message.getPartner_JID());
        Gson gson = new Gson();
        contentValues.put(MESSAGE, gson.toJson(message));

        Log.d(TAG, "addData: Adding " + message.toString() + " to " + TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            Log.e(TAG, "addData: Adding " + message.toString() + " to " + TABLE_NAME + " Failed");
            return false;
        } else {
            Log.d(TAG, "addData: Adding " + message.toString() + " to " + TABLE_NAME + " passed");
            return true;
        }
    }

    /**
     * Returns all the data from database
     *
     * @return Table rows with all columns
     */
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(query, null);
    }

    /**
     * Retrieve all communication that happened between current user and chat partner
     *
     * @param partner Chat partner JID
     * @param host    Current logged in user JID
     * @return Table rows with all columns
     */
    public Cursor getMessagesFrom(String partner, String host) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + HOST + " = '" + host + "' AND " + PARTNER + " LIKE '%" + partner + "%'";
        return db.rawQuery(query, null);
    }

    /**
     * Get unique days from conversation history
     *
     * @return Table rows with DATE column
     */
    public Cursor getDays() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + DATE + " FROM " + TABLE_NAME + " WHERE " + HOST + " ='" + host + "'" + " GROUP BY " + DATE + " ORDER BY " + DATE + " DESC";
        return db.rawQuery(query, null);
    }

    /**
     * Get messages that occurred in teh given day between host and some partner
     * @param date Examined day
     * @return Table rows with all columns
     */
    public Cursor getMessagesFromDay(Date date) {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + DATE + " ='" + dateFormat.format(date) + "' AND " + HOST + " ='" + host + "'";
        return db.rawQuery(query, null);
    }

    /**
     * Get latest (count) messages
     * @return Table rows with all columns
     */
    public Cursor getLatestMessages() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + HOST + " ='" + host + "' GROUP BY " + PARTNER + " ORDER BY " + DATE + " DESC";
        return db.rawQuery(query, null);
    }

    /**
     * Compute messages sent for each contact
     *
     * @param limit Limit of entries returned
     * @return Two columns  PPARTNER|Count of messages - sorted by Count of Messages DESC
     */
    public Cursor computeFavouriteContacts(int limit) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + PARTNER + ", " + "COUNT(" + PARTNER + ") AS FAVORITY FROM " + TABLE_NAME + " GROUP BY " + PARTNER + " ORDER BY FAVORITY DESC LIMIT " + limit;
        return db.rawQuery(query, null);
    }
}
