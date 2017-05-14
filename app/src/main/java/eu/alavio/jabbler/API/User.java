package eu.alavio.jabbler.API;

import org.jivesoftware.smack.roster.RosterGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Object for storing the information about the user.
 */

public class User extends Contact {

    private String email;
    private List<RosterGroup> groups;

    public User(String jid, String name, String email, List<RosterGroup> groups) {
        this.jid = jid;
        this.name = name;
        this.email = email;
        this.groups = groups;
    }

    public User(String jid, String name, String email) {
        this(jid, name, email, new ArrayList<>());
    }

    public String getEmail() {
        return email;
    }

}