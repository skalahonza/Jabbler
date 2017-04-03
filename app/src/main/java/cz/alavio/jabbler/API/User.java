package cz.alavio.jabbler.API;

/**
 * Created by Jan Skála on 23.03.2017.
 */

public class User {

    public User(String username, String name, String surname) {
        this.username = username;
        this.name = name;
        this.surname = surname;
    }

    private String username;
    private String name;
    private String surname;

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}
