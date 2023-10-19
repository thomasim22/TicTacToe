package test;
import server.SocketServer;

public class SocketServerTest {

    public static void main(String[] args) {
        testDefaultConstructor();
        testParameterizedConstructor();
    }

    /**
     * Method to test the default constructor
     */
    public static void testDefaultConstructor() {
        SocketServer server = new SocketServer();
        if (server.getPORT() == 5000) {
            System.out.println("Default Constructor Test: Passed");
        } else {
            System.out.println("Default Constructor Test: Failed");
        }
    }

    /**
     * Method to test the parameterized constructor
     */
    public static void testParameterizedConstructor() {
        SocketServer server = new SocketServer();
        if (server.getPORT() == 8080) {
            System.out.println("Parameterized Constructor Test: Passed");
        } else {
            System.out.println("Parameterized Constructor Test: Failed");
        }
    }
}