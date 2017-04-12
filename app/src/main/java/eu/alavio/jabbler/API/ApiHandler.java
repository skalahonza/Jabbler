package eu.alavio.jabbler.API;

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


    /** Perfroms login task communicating with the jabber server
     * @param username username ending wth @server_doman.example
     * @param password user password, non encrypted, will be encrypted in the function
     * @return true if the login was successful, false if the credentials are wrong
     */
    public static boolean login(String username, String password){

        return mockupLofin(username,password);
    }

    public static boolean mockupLofin(String username, String password){
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
                if(result){
                    //User logged in
                }
                return result;
            }
        }
        return false;
    }
}

