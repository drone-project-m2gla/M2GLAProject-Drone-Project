package entity;

import util.Constant;

/**
 * Created by mds on 07/04/15.
 * Class ${CLASS}
 */
public class User extends AbstractEntity {
    private String username;

    private String password;

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

    public User() {
        super();
    }
}
