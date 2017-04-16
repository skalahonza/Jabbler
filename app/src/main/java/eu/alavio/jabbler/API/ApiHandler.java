package eu.alavio.jabbler.API;

import android.util.Log;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Secures the communication with the server
 */

public final class ApiHandler {
    private static final String DOMAIN = "alavio.eu";
    private static final int PORT = 5222;

    private static XMPPTCPConnection connection;

    /**
     * Creates and instance of a connection to a given hostname on port 5222
     * @param source Target host e.g. alavio.eu*/
    private static XMPPTCPConnection connect(String source) {
        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
        configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        //configBuilder.setResource("Android");
        configBuilder.setServiceName(source);
        configBuilder.setHost(source);
        configBuilder.setPort(PORT);
        configBuilder.setDebuggerEnabled(true);
        return new XMPPTCPConnection(configBuilder.build());
    }

    /**
     * Initialize connection, used for loggin and other functions of XMPP
     *
     * @param source Hostname, typically alavio.eu
     * @return true if initialization is successful
     * @throws IOException    Can occur during internet connection problems
     * @throws XMPPException  XMPP Specific exception
     * @throws SmackException Library specific exception
     */
    private static boolean initConnection(String source) throws IOException, XMPPException, SmackException {
        try {
            connection = connect(source);
            connection.connect();
            return true;
        } catch (Exception ex) {
            //Log exception and pass it
            Log.e("XMPP", "Creating connection failed.", ex);
            throw ex;
        }
    }

    /**
     * Initialize connection on host alavio.eu, used for registration and other functions of app related to this particular server.
     *
     * @return true if initialization is successful
     * @throws IOException    Can occur during internet connection problems
     * @throws XMPPException  XMPP Specific exception
     * @throws SmackException Library specific exception
     */
    private static boolean initConnection() throws IOException, XMPPException, SmackException {
        return initConnection(DOMAIN);
    }

    /**
     * Perfroms login task communicating with the jabber server
     *
     * @param username username ending wth @server_doman.example
     * @param password user password, non encrypted, will be encrypted in the function
     * @return true if the login was successful, false if the credentials are wrong
     */
    public static boolean login(String username, String password) throws XMPPException, IOException, SmackException {
        String[] tmp = username.split("@");
        String source = tmp[1];
        username = tmp[0];
        try {
            Log.i("XMPP", "Initialising onnection...");
            initConnection(source);
            connection.login(username, password);
            return true;

        } catch (XMPPException | IOException | SmackException e) {
            //Not authorized
            Log.i("XMPP", "Error during login.", e);

            //unable to resolve host
            if (e.getMessage().toLowerCase().contains("java.net.unknownhostexception"))
                throw new UnknownHostException();

            throw e;
        }
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
            initConnection();

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

    public static User getCUrrentUser() throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
        //If no connection
        if (connection == null || !connection.isConnected())
            return null;

        AccountManager accountManager = AccountManager.getInstance(connection);
        String name, email, username;
        name = accountManager.getAccountAttribute("name");
        email = accountManager.getAccountAttribute("email");
        username = accountManager.getAccountAttribute("username");
        return new User(username,name,email);
    }
}

