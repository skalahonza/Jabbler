package eu.alavio.jabbler.ViewModels;

import java.util.function.Consumer;

import eu.alavio.jabbler.Models.API.Friend;

/**
 * View model for contact request used in contact fragment request ListView
 */

public class RequestContact {
    private Friend contact;
    private Consumer<String> accept;
    private Consumer<String> reject;

    public RequestContact(Friend contact, Consumer<String> accept, Consumer<String> reject) {
        this.contact = contact;
        this.accept = accept;
        this.reject = reject;
    }

    public Friend getContact() {
        return contact;
    }
}
