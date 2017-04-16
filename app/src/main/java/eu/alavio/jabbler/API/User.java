package eu.alavio.jabbler.API;

/**
 * Object for storing the information about the user.
 */

public class User {

    private String username;
    private String name;
    private String email;

    public User(String username, String name, String email) {
        this.username = username;
        this.name = name;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}