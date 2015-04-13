package fr.m2gla.istic.projet.model;

/**
 * Created by mds on 07/04/15.
 * Class ${CLASS}
 */
public class User extends Entity {
    private String username;
    private String password;

    public User() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
