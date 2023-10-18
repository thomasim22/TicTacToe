package server;

public class SocketServer {

    /**
     * @param PORT is an integer representing the last move made by the current player's opponent.
     *             The value 0-8 represents the cell of TicTacToe from top-bottom, left-right
     */
    private int PORT;

    /**
     * Static main method instantiates the classes setup() and startAcceptingRequest()
     */
    public static void main(){

        public setup(){
        // To be implemented in further milestones
        }
        public startAcceptingRequest(){
        // To be implemented in further milestones
        }
    }

    /**
     * A default constructor that calls the parameterized constructor with a default port value of 5000
     */
    public SocketServer(){
        this.PORT = 5000;
    }

    /**
     * @return the PORT attribute gotten
     */
    public int getPORT(){
        return PORT;
    }

    /**
     * A constructor that sets the port number
     */
    public void SocketServer(int PORT){
        this.PORT = PORT;
    }

}
