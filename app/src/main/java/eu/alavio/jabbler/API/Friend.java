package eu.alavio.jabbler.API;

import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.util.List;

/**
 * Object wrapper for a contact
 */

public class Friend {

    private final String user;
    private final String name;
    private final List<RosterGroup> groups;
    private VCard vCard;

    public Friend(String user, String name, List<RosterGroup> groups) {
        this.user = user;
        this.name = name;
        this.groups = groups;
    }

    public Friend(String user, String name, List<RosterGroup> groups, VCard vcard) {
        this(user, name, groups);
        this.vCard = vcard;
    }

    public String getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public List<RosterGroup> getGroups() {
        return groups;
    }
}
