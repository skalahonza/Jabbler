package eu.alavio.jabbler.Models;

import android.os.Environment;
import android.util.Log;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

import eu.alavio.jabbler.API.User;

/**
 * Class that holds dta, that could reqeusted from any scope of the app
 */

public final class AppContext {
    private static final String DOMAIN = "alavio.eu";
    private static final String HOST = "alavio.eu";
    private static final int PORT = 5222;

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

    public static boolean initConnection(String username, String password) throws IOException, XMPPException, SmackException {
        try {
            XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
            configBuilder.setUsernameAndPassword(username, password);
            configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            configBuilder.setResource("Android");
            configBuilder.setServiceName(DOMAIN);
            configBuilder.setHost(HOST);
            configBuilder.setPort(PORT);
            //configBuilder.setDebuggerEnabled(true);
            connection = new XMPPTCPConnection(configBuilder.build());
            connection.connect();
            return true;
        } catch (Exception ex) {
            //Log exception and pass it
            Log.e("XMPP","Creating connection failed.",ex);
            throw ex;
        }
    }
    public static XMPPTCPConnection getConnection() {
        return connection;
    }
}
