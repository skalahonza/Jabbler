package eu.alavio.jabbler.Models.API;

import org.jivesoftware.smack.roster.RosterGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Object for storing the information about the user.
 */

public class User extends Contact {

    private String email;
    private List<RosterGroup> groups;

    /**
     * Create new instance of user object, wrapper for current logged in user
     *
     * @param jid    User Jid
     * @param name   User name/nickname
     * @param email  User email
     * @param groups Groups of the user
     */
    public User(String jid, String name, String email, List<RosterGroup> groups) {
        this.jid = jid;
        this.name = name;
        this.email = email;
        this.groups = groups;
    }

    /**
     * Create new instance of user object, wrapper for current logged in user
     * @param jid User Jid
     * @param name User name/nickname
     * @param email User email
     */
    public User(String jid, String name, String email) {
        this(jid, name, email, new ArrayList<>());
    }

    public String getEmail() {
        return email;
    }

    @Override
    public List<RosterGroup> getGroups() {
        return groups;
    }
}