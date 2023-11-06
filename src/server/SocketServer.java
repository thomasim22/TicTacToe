package server;
/**
 * @author  thomasim22
 * @version SocketServer v1
 */
import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.IOException;
import java.net.Socket;
public class SocketServer {

    /**
     * @param PORT is an integer representing the last move made by the current player's opponent
     */
    int PORT;

    /**
     * @param serverSocket listens for client connections
     */
    ServerSocket serverSocket;

    /**
     * The static main method that instantiates the classes setup() and startAcceptingRequest()
     */
    public static void main(String[] args){
    try {
        SocketServer socketServer = new SocketServer();
        socketServer.setup();
        socketServer.startAcceptingRequest();
    }
    catch (Exception e){
        System.out.println(e.getMessage());
    }

    }

    /**
     * Default constructor that calls the default port value of 5000
     */
    public SocketServer() throws Exception{
        this(5000);
    }

    /**
     * @return PORT parameter
     */
    public int getPORT(){
        return PORT;
    }

    /**
     * Sets the constant port number
     */
    public  SocketServer(int port) throws Exception{
        if(port < 0){
            throw new Exception("Port number cannot be negative");
        }
        PORT = port;
    }

/**
     * author: @kailisacco
     * Sets up the socket server
     */
    private void setup() {
        try {
            serverSocket  = new ServerSocket(PORT);
             String hostname = InetAddress.getLocalHost().getHostName();
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Server started on:");
            System.out.println("Hostname: " + hostname);
            System.out.println("Host Address: " + hostAddress);
            System.out.println("Port: " + PORT);

        } catch (IOException ioe) {
            System.err.println("IO Exception in creating ServerSocket or closing ServerSocket");
        }
        System.out.println("Connection to port: " + PORT);
    }

    /**
     * author @kailisacco
     * Starts accepting client requests
     */
    private void startAcceptingRequest () {
        int maximumClient = 2;
        int clientCount = 0;
        try {
            Socket clientSocket = serverSocket.accept();
            String username = "Client" + (clientCount + 1);
            ServerHandler handler = new ServerHandler(clientSocket, username);

            handler.start();
            System.out.println(username + " connected from: " + clientSocket.getInetAddress().getHostAddress());
            clientCount++;
        }catch(IOException ioe){
            System.err.println("IO Exception in creating ServerSocket or closing ServerSocket");
        }

        }

}
