package test;
import socket.Request;
import socket.Response;

public class RequestTest {
    public static void main(String[] args) {
        testDefaultConstructor();
        testParameterizedConstructor();
        testGettersAndSetters();
    }

    /**
     * Method to test the default constructor
     */
    public static void testDefaultConstructor() {
        Request request = new Request();
        if (request.getType() == RequestType.REGISTER && request.getData().equals("")) {
            System.out.println("Default Constructor Test: Passed");
        } else {
            System.out.println("Default Constructor Test: Failed");
        }
    }

    /**
     * Method to test the parameterized constructor
     */
    public static void testParameterizedConstructor() {
        Request request = new Request(RequestType.LOGIN, "Test message");
        if (request.getType() == RequestType.LOGIN && request.getData().equals("Test message")) {
            System.out.println("Parameterized Constructor Test: Passed");
        } else {
            System.out.println("Parameterized Constructor Test: Failed");
        }
    }

    /**
     * Method to test the getters and setters
     */
    public static void testGettersAndSetters() {
        Request request = new Request();
        request.setType(RequestType.ABORT_GAME);
        request.setData("New message");
        if (request.getType() == RequestType.ABORT_GAME && request.getData().equals("New message")) {
            System.out.println("Getters and Setters Test: Passed");
        } else {
            System.out.println("Getters and Setters Test: Failed");
        }
    }

}

