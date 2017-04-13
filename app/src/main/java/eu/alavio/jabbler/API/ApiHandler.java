package eu.alavio.jabbler.API;

import android.util.Log;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
     * @param source   Hostname, typically alavio.eu
     * @return true if initialization is successful
     * @throws IOException    Can occur during internet connection problems
     * @throws XMPPException  XMPP Specific exception
     * @throws SmackException Library specific exception
     */
    private static boolean initConnection(String username, String password, String source) throws IOException, XMPPException, SmackException {
        try {
            XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
            //configBuilder.setUsernameAndPassword(username, password);
            configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            //configBuilder.setResource("Android");
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

    /**
     * Initialize connection on host alavio.eu, used for loggin and other functions of XMPP
     *
     * @param username with domain - example: test@domain.com
     * @param password pure text (non encrypted)
     * @return true if initialization is successful
     * @throws IOException    Can occur during internet connection problems
     * @throws XMPPException  XMPP Specific exception
     * @throws SmackException Library specific exception
     */
    private static boolean initConnection(String username, String password) throws IOException, XMPPException, SmackException {
        return initConnection(username, password, DOMAIN);
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
                connection.login(username,password);
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

    /**
     * Tries to logout from the current XMPP session
     *
     * @return True if the logout was succesful
     */
    public static boolean logout() {
        if (connection == null)
            return false;
        try {
            connection.disconnect();
            String user = connection.getUser();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean register(String username, String password, String email, String fullName) throws XMPPException, IOException, SmackException {
        //If no connection, create a new one
        if (connection == null || !connection.isConnected())
            initConnection(username, password);

        AccountManager accountManager = AccountManager.getInstance(connection);
        //creation not supported
        if (!accountManager.supportsAccountCreation()) return false;

        //TODO For insecure connection (temporary)
        accountManager.sensitiveOperationOverInsecureConnection(true);
        Map<String, String> attributes = new HashMap<>();
        attributes.put("name", fullName);
        attributes.put("email", email);
        accountManager.createAccount(username, password, attributes);
        return true;
    }
}

