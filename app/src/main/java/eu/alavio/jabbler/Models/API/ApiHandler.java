package eu.alavio.jabbler.Models.API;

import android.util.Log;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.alavio.jabbler.Models.Helpers.Helper;

/**
 * Secures the communication with the server
 */

public final class ApiHandler {
    private static final String DOMAIN = "alavio.eu";
    private static final int PORT = 5222;

    private static XMPPTCPConnection connection;
    private static String currentChatPartner = null;

    /**
     * Creates and instance of a connection to a given hostname on port 5222
     *
     * @param source Target host e.g. alavio.eu
     */
    private static XMPPTCPConnection connect(String source) {
        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
        configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
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
            Log.e("XMPP", "Error during login.", e);

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

    /**
     * Register new user into alavio.eu server
     *
     * @param username Username - without domain
     * @param password user password
     * @param email    user contact email, different rom username and domain, e.g. sample@alavio.eu is not different email
     * @param fullName Name to be displayed in a profile and so on
     * @return True if success
     * @throws XMPPException
     * @throws IOException
     * @throws SmackException
     */
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

    /**
     * Get logged in user's data
     *
     * @return Current user data Wrapped in User class
     * @throws SmackException.NotConnectedException
     * @throws XMPPException.XMPPErrorException
     * @throws SmackException.NoResponseException
     */
    public static User getCurrentUser() throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
        //If no connection
        if (connection == null || !connection.isConnected())
            throw new SmackException.NotConnectedException();

        AccountManager accountManager = AccountManager.getInstance(connection);
        String name, email, username;
        name = accountManager.getAccountAttribute("name");
        email = accountManager.getAccountAttribute("email");
        username = accountManager.getAccountAttribute("username") + "@" + connection.getHost();
        return new User(username, name, email);
    }

    /**
     * Iterates through roster of current user and returns wrapped contacts
     *
     * @return List of Friend objects (contacts from roster)
     * @throws SmackException.NotLoggedInException
     * @throws SmackException.NotConnectedException
     * @throws XMPPException.XMPPErrorException
     * @throws SmackException.NoResponseException
     */
    public static List<Friend> getMyContacts() throws SmackException.NotLoggedInException, SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
        Roster roster = getCurrentRoster();

        List<Friend> contacts = new ArrayList<>();
        VCardManager vCardManager = VCardManager.getInstanceFor(connection);

        for (RosterEntry entry : roster.getEntries()) {
            if (entry.getName() != null) {
                VCard vCard = vCardManager.loadVCard(entry.getUser());
                contacts.add(new Friend(entry.getUser(), entry.getName(), entry.getGroups(), vCard));
            }
        }

        return contacts;
    }

    /**
     * Searches roster for given jabber id and returns wrapped contact if found, used in android navigation
     *
     * @param jid Jabber id of the contact
     * @return Wrapped roster contact in Friend class, null if not found in contact list
     * @throws SmackException.NotConnectedException
     * @throws XMPPException.XMPPErrorException
     * @throws SmackException.NoResponseException
     */
    public static Friend getContact(String jid) throws SmackException.NotConnectedException, XMPPException.XMPPErrorException, SmackException.NoResponseException, SmackException.NotLoggedInException {
        Roster roster = getCurrentRoster();
        RosterEntry entry = roster.getEntry(jid);
        if (entry == null) return null;

        VCardManager vCardManager = VCardManager.getInstanceFor(connection);
        VCard vCard = vCardManager.loadVCard(entry.getUser());
        return new Friend(entry.getUser(), entry.getName(), entry.getGroups(), vCard);
    }

    /**
     * Adds contact to roster of current User
     *
     * @param jid      Jabber id of contact
     * @param nickname Nickname to be displayed in contact list
     * @param groups   Groups the user is registered in
     * @throws SmackException.NotLoggedInException
     * @throws XMPPException.XMPPErrorException
     * @throws SmackException.NotConnectedException
     * @throws SmackException.NoResponseException
     */
    public static boolean addContact(String jid, String nickname, String[] groups) throws Exception {
        Roster roster = getCurrentRoster();
        if (!Helper.validateEmail(jid))
            throw new Exception("Invalid username given");
        RosterEntry tmp = roster.getEntry(jid);
        if (tmp == null) {
            roster.createEntry(jid, nickname, groups);
            return true;
        } else {
            Log.e("ApiHandler", "Cannot add: " + jid + " Contact already added");
            return false;
        }
    }

    /**
     * Removes contact from current user's roster (contact list)
     *
     * @param contact Contact/Friend to be deleted
     * @throws SmackException.NotLoggedInException
     * @throws XMPPException.XMPPErrorException
     * @throws SmackException.NotConnectedException
     * @throws SmackException.NoResponseException
     */
    public static void removeContact(Friend contact) throws SmackException.NotLoggedInException, XMPPException.XMPPErrorException, SmackException.NotConnectedException, SmackException.NoResponseException {
        removeContact(contact.getJid());
    }

    /**
     * Remove contact rom roster by given JID
     *
     * @param jid JID of contact to be deleted
     * @throws SmackException.NotConnectedException
     * @throws SmackException.NotLoggedInException
     * @throws XMPPException.XMPPErrorException
     * @throws SmackException.NoResponseException
     */
    public static void removeContact(String jid) throws SmackException.NotConnectedException, SmackException.NotLoggedInException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
        Roster roster = getCurrentRoster();
        RosterEntry tmp = roster.getEntry(jid);
        if (tmp != null)
            roster.removeEntry(tmp);
    }

    /**
     * Change contact nickname
     *
     * @param jid      Jid of the contact, that should by edited
     * @param nickname New nickname for the contact
     * @throws SmackException.NotConnectedException
     * @throws SmackException.NotLoggedInException
     * @throws XMPPException.XMPPErrorException
     * @throws SmackException.NoResponseException
     */
    public static void updateContactNickname(String jid, String nickname) throws SmackException.NotConnectedException, SmackException.NotLoggedInException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
        Roster roster = getCurrentRoster();
        roster.createEntry(jid, nickname, null);
    }

    /**
     * Inits chat with a given contact
     *
     * @param partner Friend to begin chat wit
     * @return Chat wrapper object
     */
    public static Chat initChat(Friend partner) {
        return initChat(partner.getJid());
    }

    /**
     * Inits chat with a given contact
     *
     * @param jid Chat partner to begin chat with JID
     * @return Chat wrapper object
     */
    public static Chat initChat(String jid) {
        if (connection.isAuthenticated()) {
            Log.i("Chat init", "Authenticated, creating chat with: " + jid);
            ChatManager chatManager = ChatManager.getInstanceFor(connection);
            currentChatPartner = jid;
            return chatManager.createChat(jid);
        } else {
            Log.e("Chat init", "Chat init failed due to missing authentication.");
            return null;
        }
    }

    /**
     * Ends current chat that happens in ChatFragment
     */
    public static void endCurrentChat() {
        currentChatPartner = null;
    }

    /**
     * Get chat manager for current user
     *
     * @return Null if error
     */
    public static ChatManager backgroundChatManager() {
        if (connection.isAuthenticated()) {
            Log.i(ApiHandler.class.getName(), "Creating chat manager");
            return ChatManager.getInstanceFor(connection);
        } else {
            Log.e(ApiHandler.class.getName(), "Chat init failed due to missing authentication.");
            return null;
        }
    }

    /**
     * Get roster for current user
     *
     * @return Null if not logged in
     */
    public static Roster getCurrentRoster() throws SmackException.NotConnectedException, SmackException.NotLoggedInException {
        if (connection == null) throw new SmackException.NotConnectedException();

        Roster roster = Roster.getInstanceFor(connection);
        if (!roster.isLoaded())
            roster.reloadAndWait();
        return roster;
    }

    /**
     * Search roster for contact requests
     *
     * @return Collection of users that wants to subscribe to current user
     * @throws SmackException.NotConnectedException
     * @throws SmackException.NotLoggedInException
     * @throws XMPPException.XMPPErrorException
     * @throws SmackException.NoResponseException
     */
    public static List<Friend> getContactRequests() throws SmackException.NotConnectedException, SmackException.NotLoggedInException, XMPPException.XMPPErrorException, SmackException.NoResponseException {
        Roster roster = getCurrentRoster();

        List<Friend> contacts = new ArrayList<>();
        VCardManager vCardManager = VCardManager.getInstanceFor(connection);

        for (RosterEntry entry : roster.getEntries()) {
            if (entry.getName() == null) {
                VCard vCard = vCardManager.loadVCard(entry.getUser());
                contacts.add(new Friend(entry.getUser(), entry.getName(), entry.getGroups(), vCard));
            }
        }

        return contacts;
    }

    /**
     * Check if the user is now chatting with the given JID
     *
     * @param jid Examined JID, obtain it from incoming message
     * @return True if the user is having a chat with the given JID in a chat window
     */
    public static boolean isChatInProgress(String jid) {
        return jid.equals(currentChatPartner);
    }
}

