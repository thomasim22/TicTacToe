package model;
import java.lang.String;

public class User {
    private String username;
    private String password;
    private String displayName;
    private boolean online;

    public User() {
    }

    public User(String username, String password, String displayName, boolean online){
    }

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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    /*
    equals() override
     */
}
