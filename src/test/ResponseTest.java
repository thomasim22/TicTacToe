package test;
import socket.Response;
import socket.ResponseStatus;
/**
 * @author  thomasim22
 * @version ResponseTest v1
 */

/**
 * A test class for the Response class.
 */
public class ResponseTest {

    public static void main(String[] args) {
        testDefaultConstructor();
        testParameterizedConstructor();
        testGettersAndSetters();
    }

    /**
     * Method to test the default constructor
     */
    public static void testDefaultConstructor() {
        Response response = new Response();
        if (response.getStatus() == ResponseStatus.FAILURE && response.getMessage().equals("")) {
            System.out.println("Default Constructor Test: Passed");
        } else {
            System.out.println("Default Constructor Test: Failed");
        }
    }

    /**
     * Method to test the parameterized constructor
     */
    public static void testParameterizedConstructor() {
        Response response = new Response(ResponseStatus.SUCCESS, "Test message");
        if (response.getStatus() == ResponseStatus.SUCCESS && response.getMessage().equals("Test message")) {
            System.out.println("Parameterized Constructor Test: Passed");
        } else {
            System.out.println("Parameterized Constructor Test: Failed");
        }
    }

    /**
     * Method to test the getters and setters
     */
    public static void testGettersAndSetters() {
        Response response = new Response();
        response.setStatus(ResponseStatus.SUCCESS);
        response.setMessage("New message");
        if (response.getStatus() == ResponseStatus.SUCCESS && response.getMessage().equals("New message")) {
            System.out.println("Getters and Setters Test: Passed");
        } else {
            System.out.println("Getters and Setters Test: Failed");
        }
    }
}