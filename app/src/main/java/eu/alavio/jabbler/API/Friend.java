package eu.alavio.jabbler.API;

import org.jivesoftware.smack.roster.RosterGroup;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;

import java.util.List;

/**
 * Object wrapper for a contact
 */

public class Friend {

    private final String jid;
    private final String nickname;
    private final List<RosterGroup> groups;
    private VCard vCard;

    public Friend(String jid, String nickname, List<RosterGroup> groups) {
        this.jid = jid;
        this.nickname = nickname;
        this.groups = groups;
    }

    public Friend(String jid, String nickname, List<RosterGroup> groups, VCard vcard) {
        this(jid, nickname, groups);
        this.vCard = vcard;
    }

    public String getJid() {
        return jid;
    }

    public String getNickname() {
        return nickname;
    }

    public List<RosterGroup> getGroups() {
        return groups;
    }
}
