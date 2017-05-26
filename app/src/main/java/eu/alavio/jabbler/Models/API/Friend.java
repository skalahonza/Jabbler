package eu.alavio.jabbler.Models.API;

import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.util.List;

/**
 * Object wrapper for a contact
 */

public class Friend extends Contact{

    private VCard vCard;

    /**
     * Create new instance of Friend object wrapper for contact
     *
     * @param jid    User Jid
     * @param name   User name/nickname
     * @param groups Groups of the user
     */
    public Friend(String jid, String name, List<RosterGroup> groups) {
        this.jid = jid;
        this.name = name;
        this.groups = groups;
    }

    /**
     * Create new instance of Friend object wrapper for contact
     * @param jid User Jid
     * @param name User name/nickname
     * @param groups Groups of the user
     * @param vcard Vcard of teh contact, obtained from Roster
     */
    public Friend(String jid, String name, List<RosterGroup> groups, VCard vcard) {
        this(jid, name, groups);
        this.vCard = vcard;
    }

    public VCard getvCard() {
        return vCard;
    }
}
