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
     * author: @kailisacco
     * Sets up the socket server
     */
    private void setup() {
        try {
            ServerSocket sskt = new ServerSocket(PORT);
            Socket clientSkt = sskt.accept();
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSkt.getInputStream()));
            PrintWriter outToClient = new PrintWriter(clientSkt.getOutputStream(), true); // auto flush

            outToClient.println("From Server: Launched setup() from SocketServer.");

            inFromClient.close();
            outToClient.close();
            clientSkt.close();
            sskt.close();
        } catch (IOException ioe) {
            System.err.println("IO Exception in creating ServerSocket or closing ServerSocket");
            ioe.printStackTrace();
        }
        System.out.println("Connection to port: " + PORT);
    }

    /**
     * author @kailisacco
     * Starts accepting clinet requests
     */
    private void startAcceptingRequest () {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            for (int i = 0; i < 2; i++) {
                Socket clientSocket = serverSocket.accept();
                String username = "User" + i;

                ServerHandler handlerThread = new ServerHandler(clientSocket, username);
                handlerThread.start();
                System.out.println("Accepted connection from " + username);
            }
            serverSocket.close();
        }catch(IOException ioe){
            System.err.println("IO Exception in creating ServerSocket or closing ServerSocket");
        }

        }

}
