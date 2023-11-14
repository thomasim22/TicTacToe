package server;

import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The `SocketServer` class represents a socket server controller. It creates the socket server
 * and accepts client connections.
 */
public class SocketServer {
    /**
     * Used for printing server logs of different levels
     */
    private final Logger LOGGER;

    /*
    The socket server's port number
     */
    private final int PORT;

    /**
     * ServerSocket instance
     */
    private ServerSocket serverSocket;

    /**
     * The main function of the application
     * It instantiates the class, sets up the server and start accepting client's request
     *
     * @param args command line arguments
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
     * Default constructor with default port = 5000
     *
     * @throws Exception when invalid port is provided
     */
    public SocketServer() throws Exception {
        this(6000);
    }

    /**
     * Constructor that set the {@link #PORT} attribute
     *
     * @param port The socket server's port number
     * @throws Exception when invalid port is provided
     */
    public SocketServer(int port) throws Exception {
        if (port < 0) {
            throw new Exception("Port number cannot be negative");
        }
        PORT = port;
        LOGGER = Logger.getLogger(SocketServer.class.getName());
    }

    /**
     * Sets up the socket server
     */
    private void setup() {
        try {
            serverSocket = new ServerSocket(PORT);
            LOGGER.log(Level.INFO, "Server Initialization Succeeded"
                    + "\nServer Host Name: " + InetAddress.getLocalHost().getHostName()
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
     * Start accepting client's request
     */
    public void startAcceptingRequest() {
        // Removed the maxClients limit and clientCount

        while (true) { // Infinite loop to keep accepting client connections
            try {
                // Accept client connection
                Socket clientSocket = serverSocket.accept();

                // You could use a random or incremental approach to generate unique usernames
                // This is a placeholder approach, and should be replaced with your actual logic
                String username = "Client" + System.currentTimeMillis();

                // Create a new instance of the ServerHandler class
                ServerHandler handler = new ServerHandler(clientSocket);

                // Start the handler thread
                new Thread(handler).start(); // Make sure ServerHandler implements Runnable

                // Log client information
                System.out.println(username + " connected from: " + clientSocket.getInetAddress().getHostAddress());

                // clientCount is no longer needed since we are accepting infinite connections

            } catch (IOException e) {
                System.err.println("Error accepting client connection: " + e.getMessage());
                // Consider adding a mechanism to break out of the loop upon a severe failure
                // that requires the server to stop accepting new connections
            }
        }
    }

    /**
     * Getter for PORT attribute
     *
     * @return PORT
     */
    public int getPORT() {
        return PORT;
    }
}
