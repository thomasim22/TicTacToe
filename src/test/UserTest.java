package test;
import model.User;
/**
 * @author  kailisacco
 * @version UserTest v1
 */
public class UserTest {
    public static void main(String[] args) {
        testDefaultConstructor();
        testParameterizedConstructor();
        testGettersAndSetters();
        testEqualsMethod();
    }

    /**
     * Tests the default constructor
     */
    private static void testDefaultConstructor() {
        User user = new User();
        System.out.println("Testing Default Constructor:");
        System.out.println("Username: " + user.getUsername().equals(""));
        System.out.println("Password: " + user.getPassword().equals(""));
        System.out.println("Display Name: " + user.getDisplayName().equals(""));
        System.out.println("Online: " + !user.isOnline());
    }

    /**
     * Tests the Parameterized Constructor
     */
    private static void testParameterizedConstructor() {
        User user = new User("john_doe", "password123", "John Doe", true);
        System.out.println("Testing Parameterized Constructor:");
        System.out.println("Username: " + user.getUsername().equals("john_doe"));
        System.out.println("Password: " + user.getPassword().equals("password123"));
        System.out.println("Display Name: " + user.getDisplayName().equals("John Doe"));
        System.out.println("Online: " + user.isOnline());
    }
    /**
     * Tests all the getters and setters
     */
    private static void testGettersAndSetters() {
        User user = new User();
        user.setUsername("jon_smith");
        user.setPassword("newPassword");
        user.setDisplayName("Jon Smith");
        user.setOnline(true);

        System.out.println("Testing Getters and Setters:");
        System.out.println("Username: " + user.getUsername().equals("alice_smith"));
        System.out.println("Password: " + user.getPassword().equals("newPassword"));
        System.out.println("Display Name: " + user.getDisplayName().equals("Alice Smith"));
        System.out.println("Online: " + user.isOnline());
    }
    /**
     * Tests the equals() method
     */
    private static void testEqualsMethod() {
        User user1 = new User("user1", "password1", "User One", false);
        User user2 = new User("user2", "password2", "User Two", true);
        User user3 = new User("user1", "password1", "User One", true);

        System.out.println("Testing Equals Method:");
        System.out.println("user1.equals(user3): " + user1.equals(user3));
        System.out.println("user1.equals(user2): " + user1.equals(user2));
    }
}