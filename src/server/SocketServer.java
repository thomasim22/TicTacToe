package server;
/**
 * @author  thomasim22
 * @version SocketServer v1
 */
public class SocketServer {

    /**
     * @param PORT is an integer representing the last move made by the current player's opponent
     */
    private final int PORT;

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
     * Sets up the socket server
     */
    private void setup(){

    }

    /**
     * Starts accepting clinet requests
     */
    private void startAcceptingRequest(){

    }

}
