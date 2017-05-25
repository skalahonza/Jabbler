package eu.alavio.jabbler.ViewModels;

import eu.alavio.jabbler.Models.API.Friend;

/**
 * View model for contact request used in contact fragment request ListView
 */

public class ContactRequest {
    private Friend contact;

    public ContactRequest(Friend contact) {
        this.contact = contact;
    }

    public Friend getContact() {
        return contact;
    }
}
