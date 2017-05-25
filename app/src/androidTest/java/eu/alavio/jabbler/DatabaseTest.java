package eu.alavio.jabbler;

import android.database.Cursor;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import org.jivesoftware.smack.packet.Message;
import org.junit.Test;

import eu.alavio.jabbler.Models.API.ChatMessage;
import eu.alavio.jabbler.Models.Helpers.DatabaseHelper;

/**
 * Android test for DatabaseHelper
 */

public class DatabaseTest extends AndroidTestCase {

    private DatabaseHelper db;
    private final String ALICE = "alice@alavio.eu";
    private final String BOB = "bob@alavio.eu";

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        db = new DatabaseHelper(context, ALICE);

        db.addData(ChatMessage.ToBeSendMessage(mockMessage(ALICE, BOB, "Hi")));
        db.addData(ChatMessage.ReceivedMessage(mockMessage(BOB, ALICE, "Hello")));

        db.addData(ChatMessage.ToBeSendMessage(mockMessage(ALICE, BOB, "How are you ?")));
        db.addData(ChatMessage.ReceivedMessage(mockMessage(BOB, ALICE, "Fine, and you ?")));

        db.addData(ChatMessage.ToBeSendMessage(mockMessage(ALICE, BOB, "Me too, thanks.")));
        db.addData(ChatMessage.ReceivedMessage(mockMessage(BOB, ALICE, "Bye.")));
        db.addData(ChatMessage.ToBeSendMessage(mockMessage(ALICE, BOB, "By bye.")));
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }

    @Test
    public void testGetAll() {
        Cursor data = db.getAllData();
        int count = 0;
        while (data.moveToNext()) {
            count++;
        }
        assertEquals(7, count);
    }

    @Test
    public void testDetermineFavouriteContacts() {
        Cursor data = db.computeFavouriteContacts(1);
        String favourite = "";
        int count = 0;
        while (data.moveToNext()) {
            favourite = data.getString(0); // get the most favourite
            count++;
        }
        assertEquals(1, count);
        assertEquals(BOB, favourite);
    }

    private Message mockMessage(String from, String to, String body) {
        Message message = new Message();
        message.setFrom(from);
        message.setTo(to);
        message.setBody(body);
        return message;
    }
}
