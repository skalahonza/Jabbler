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

    /**
     * A dummy authentication store containing known user names and passwords.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "test@alavio.eu:Test12345", "bar@example.com:world"
    };

    private static final String DOMAIN = "alavio.eu";
    private static final String HOST = "alavio.eu";
    private static final int PORT = 5222;

    private static XMPPTCPConnection connection;

    private static boolean initConnection(String username, String password) throws IOException, XMPPException, SmackException {
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

    /**
     * Perfroms login task communicating with the jabber server
     *
     * @param username username ending wth @server_doman.example
     * @param password user password, non encrypted, will be encrypted in the function
     * @return true if the login was successful, false if the credentials are wrong
     */
    public static boolean login(String username, String password) {
        try {
            if (initConnection(username, password))
                connection.login(username, password);
            return true;
        } catch (XMPPException e) {
            //Not authorized
            Log.i("XMPP","Login, not authorized.",e);
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //wrong login
        return false;
    }

    public static boolean mockupLogin(String username, String password) {
        //mockup login
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (String credential : DUMMY_CREDENTIALS) {
            String[] pieces = credential.split(":");
            if (pieces[0].equals(username)) {
                // Account exists, return true if the password matches.
                boolean result = pieces[1].equals(password);
                if (result) {
                    //User logged in
                }
                return result;
            }
        }
        return false;
    }
}

