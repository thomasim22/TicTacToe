package server;
/**
 * @author  thomasim22
 * @version SocketServer v1
 */
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
public class SocketServer {

    /**
     * @param PORT is an integer representing the last move made by the current player's opponent
     */
    private final int PORT;

    private final Logger LOGGER;

    /**
     * @param serverSocket listens for client connections
     */
    private ServerSocket serverSocket;

    /**
     * The static main method that instantiates the classes setup() and startAcceptingRequest()
     */
    public static void main(String[] args) {
        try {
            SocketServer socketServer = new SocketServer();
            socketServer.setup();
            socketServer.startAcceptingRequest();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Default constructor that calls the default port value of 5000
     */
    public SocketServer() throws Exception {
        this(5000);
    }

    /**
     * @return PORT parameter
     */
    public int getPORT() {
        return PORT;
    }

    /**
     * Sets the constant port number
     */
    public SocketServer(int port) throws Exception {
        if (port < 0) {
            throw new Exception("Port number cannot be negative");
        }
        PORT = port;
        LOGGER = Logger.getLogger(SocketServer.class.getName());
    }

    /**
     * author: @kailisacco
     * Sets up the socket server
     */
    private void setup() {
        try {
            serverSocket = new ServerSocket(PORT);
            LOGGER.log(Level.INFO, "Server Initialization Succeeded"
                    + "\nServer Host name: " + InetAddress.getLocalHost().getHostName()
                    + "\nServer IP: " + InetAddress.getLocalHost().getHostAddress()
                    + "\nServer Port Number: " + PORT);
        } catch (UnknownHostException e) {
            LOGGER.log(Level.SEVERE, "Server Error: Unable to Resolve Host", e);
            System.exit(1);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Server Error: Server Initialization Failed", e);
            System.exit(1);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Server Error: Unknown Exception Occurred", e);
            System.exit(1);
        }
    }

    /**
     * author @kailisacco
     * Starts accepting client requests
     */
    private void startAcceptingRequest() {
        try {
            // Accept socket connection from the first player and create a new handler to handle all connections
            Socket socketPlayer1 = serverSocket.accept();
            LOGGER.log(Level.INFO, "New Socket Client Connect with IP: " + socketPlayer1.getRemoteSocketAddress());
            ServerHandler serverHandlerPlayer1 = new ServerHandler(socketPlayer1, "Bob");
            serverHandlerPlayer1.start();

            // Accept socket connection from the second player and create a new handler to handle all connections
            Socket socketPlayer2 = serverSocket.accept();
            LOGGER.log(Level.INFO, "New Socket Client Connect with IP: " + socketPlayer2.getRemoteSocketAddress());
            ServerHandler serverHandlerPlayer2 = new ServerHandler(socketPlayer2, "Smith");
            serverHandlerPlayer2.start();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Server Error: Client Connection Failed", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Server Error: Unknown Exception Occurred", e);
        }
    }
}