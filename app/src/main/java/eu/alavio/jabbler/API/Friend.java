package eu.alavio.jabbler.API;

import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.util.List;

/**
 * Object wrapper for a contact
 */

public class Friend extends Contact{

    private VCard vCard;

    public Friend(String jid, String name, List<RosterGroup> groups) {
        this.jid = jid;
        this.name = name;
        this.groups = groups;
    }

    public Friend(String jid, String name, List<RosterGroup> groups, VCard vcard) {
        this(jid, name, groups);
        this.vCard = vcard;
    }
}
