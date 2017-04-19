package eu.alavio.jabbler.API;

/**
 * Created by Jan Skála on 19.04.2017.
 */

public class Friend extends User {
    private String nickname;

    public Friend(String username, String name, String email, String nickname) {
        super(username, name, email);
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
