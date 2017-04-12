package eu.alavio.jabbler.Models;

import android.os.Environment;
import android.util.Log;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import eu.alavio.jabbler.API.User;

/**
 * Class that holds dta, that could reqeusted from any scope of the app
 */

public final class AppContext {
    private static final String MAIN_URL = "http://alavio.eu:5222";
    private static User currentUser = null;
    private static XMPPTCPConnection connection;

    /**
     * Checks if external storage is available for read and write
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * Checks if external storage is available to read
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public boolean initConnection(String username, String password, String server) {
        try {
            connection = new XMPPTCPConnection(username, password, MAIN_URL);
            connection.connect();
            return true;
        } catch (Exception ex) {
            Log.e(this.getClass().getName(),"Creating connection failed.",ex);
            return false;
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static XMPPTCPConnection getConnection() {
        return connection;
    }
}
