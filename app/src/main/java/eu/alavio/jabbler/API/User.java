package eu.alavio.jabbler.API;

import org.jivesoftware.smackx.vcardtemp.packet.VCard;

/**
 * Object for storing the information about the user.
 */

public class User {

    private String username;
    private String name;
    private String email;
    private String[] groups;

    public User(String username, String name, String email, String[] groups) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.groups = groups;
    }

    public User(String username, String name, String email) {
        this(username, name, email, new String[]{});
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String[] getGroups() {
        return groups;
    }
}