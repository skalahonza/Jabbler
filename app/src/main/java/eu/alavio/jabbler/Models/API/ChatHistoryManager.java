package eu.alavio.jabbler.Models.API;

import java.util.Collection;

/**
 * Created by Jan Skála on 22.05.2017.
 */

public class ChatHistoryManager {
    private User user;

    public ChatHistoryManager(User user) {
        this.user = user;

        //TODO INIT STORAGE CONNECTION
    }

    public void saveMessage(ChatMessage message) {

    }

    public Collection<ChatMessage> getMessagesFrom(Friend contact) {
        return null;
    }
}
