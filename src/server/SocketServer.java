package server;

public class SocketServer {

    /**
     * @param PORT is an integer representing the last move made by the current player's opponent
     */
    private int PORT;

    /**
     * The static main method that instantiates the classes setup() and startAcceptingRequest()
     */
    public static void main(String[] args){
    }

    public startAcceptingRequest(){
        // To be implemented in further milestones
    }

    public setup(){
        // To be implemented in further milestones
    }

    /**
     * Default constructor that calls the default port value of 5000
     */
    public SocketServer(){
        this.PORT = 5000;
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
    public void SocketServer(int PORT){
        this.PORT = PORT;
    }

}
