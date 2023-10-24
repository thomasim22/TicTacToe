package model;
import java.lang.String;

/**
 * @author kailisacco
 * @version User.java v1
 */
public class User {
    private String username;
    private String password;
    private String displayName;
    private boolean online;


    /**
     *  default constructor for User() class with generic parameters
     */
    public User() {
    }


    /**
     * @param username      A string representation of user’s username
     * @param password      A string representation of user’s password
     * @param displayName   A string representation of user’s display name
     * @param online        A Boolean variable to indicate if a user is online or not
     */
    public User(String username, String password, String displayName, boolean online){
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.online = online;
    }


    /**
     * @return  returns the string representation of user’s username
     */
    public String getUsername() {
        return username;
    }


    /**
     * @param username  assigns the string representation of user’s username
     */
    public void setUsername(String username) {
        this.username = username;
    }


    /**
     * @return returns the string representation of user’s password
     */
    public String getPassword() {
        return password;
    }


    /**
     * @param password  assigns the string representation of user’s password
     */
    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * @return returns the string representation of user’s display name
     */
    public String getDisplayName() {
        return displayName;
    }


    /**
     * @param displayName assigns the string representation of user’s display name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    /**
     * @return returns a Boolean variable to indicate if a user is online or not
     */
    public boolean isOnline() {
        return online;
    }


    /**
     * @param online assigns a Boolean variable to indicate if a user is online or not
     */
    public void setOnline(boolean online) {
        this.online = online;
    }


    /**
     * @param obj     represents another User object and its attributes
     * @return          returns boolean value after comparing two User object's username attribute
     */
    public boolean equals(Object obj){
        try {
            User other = (User) obj;
            return this.username.equals(other.getUsername());
        }
        catch (ClassCastException e){
            return false;
        }
    }
}