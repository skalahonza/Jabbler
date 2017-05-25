package eu.alavio.jabbler.Models.API;

import org.jivesoftware.smack.roster.RosterGroup;

import java.util.List;

/**
 * Interfaces that groups common access fields of current User and Contact in a roster
 */

public abstract class Contact {
    protected String jid;
    protected String name;
    protected List<RosterGroup> groups;

    public void setJid(String jid) {
        this.jid = jid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGroups(List<RosterGroup> groups) {
        this.groups = groups;
    }

    public String getJid() {
        return jid;
    }

    public String getName() {
        return name;
    }

    public List<RosterGroup> getGroups() {
        return groups;
    }
}
