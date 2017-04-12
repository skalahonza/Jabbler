package eu.alavio.jabbler.API;

import android.util.Log;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

import eu.alavio.jabbler.Models.AppContext;

/**
 * Secures the communication with the server
 */

public final class ApiHandler {
    private static final String DOMAIN = "alavio.eu";
    private static final String HOST = "alavio.eu";
    private static final int PORT = 5222;

    private static XMPPTCPConnection connection;

    /**
     * Initialize connection, used for loggin and other functions of XMPP
     *
     * @param username with domain - example: test@domain.com
     * @param password pure text (non encrypted)
     * @return true if initialization is successful
     * @throws IOException    Can occur during internet connection problems
     * @throws XMPPException  XMPP Specific exception
     * @throws SmackException Library specific exception
     */
    private static boolean initConnection(String username, String password, String source) throws IOException, XMPPException, SmackException {
        try {
            XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
            configBuilder.setUsernameAndPassword(username, password);
            configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            configBuilder.setResource("Android");
            configBuilder.setServiceName(source);
            configBuilder.setHost(source);
            configBuilder.setPort(PORT);
            //configBuilder.setDebuggerEnabled(true);
            connection = new XMPPTCPConnection(configBuilder.build());
            connection.connect();
            return true;
        } catch (Exception ex) {
            //Log exception and pass it
            Log.e("XMPP", "Creating connection failed.", ex);
            throw ex;
        }
    }

    private static boolean initConnection(String username, String password) throws IOException, XMPPException, SmackException {
        return initConnection(username,password,DOMAIN);
    }

    /**
     * Perfroms login task communicating with the jabber server
     *
     * @param username username ending wth @server_doman.example
     * @param password user password, non encrypted, will be encrypted in the function
     * @return true if the login was successful, false if the credentials are wrong
     */
    public static boolean login(String username, String password) {
        String[] tmp = username.split("@");
        String source = tmp[1];
        username = tmp[0];
        try {
            Log.i("XMPP", "Initialising onnection...");
            if (initConnection(username, password, source)) {
                connection.login();
                return true;
            }
        } catch (XMPPException e) {
            //Not authorized
            Log.i("XMPP", "Login, not authorized.", e);
            return false;
        } catch (SmackException e) {
            Log.e("XMPP", "Library exception.", e);
            return false;
        } catch (IOException e) {
            Log.e("XMPP", "Connection exception.", e);
            return false;
        }

        // credentials rejected
        return false;
    }
}

