package server;
/**
 * @author  thomasim22 declaration
 *          kailisacco implementation
 * @version ServerHandler v1
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import model.Event;
import socket.GamingResponse;
import socket.Request;
import socket.Response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that helps SocketServer handle individual communications from users.
 */
public class ServerHandler extends Thread{

    private final Logger LOGGER;

    /**
     * Socket in which the server communicates with the client
     */
    private final Socket socket;

    /**
     * The username of the client
     */
    private String currentUsername;

    /**
     * Input stream to read client data
     */
    private final DataInputStream dataInput;

    /**
     * Output stream to send client data
     */
    private final DataOutputStream dataOutput;

    /**
     * Gson object to handle serialization and deserialization
     */
    private Gson gson;

    /**
     * Vairbale to store the game move and the player who made the move
     */
    private static Event gameEvent = new Event(1, null, null, null, null, -1);

    /**
     * ServerHandler constructor that accepts a Socket and username
     */
    public ServerHandler(Socket socket, String username) throws IOException{
        LOGGER = Logger.getLogger(ServerHandler.class.getName());

        this.socket = socket;
        this.currentUsername = username;
        // Initialize the Gson object with a configuration that allows serializing null values
        this.gson = new GsonBuilder().serializeNulls().create();
        this.dataInput = new DataInputStream(socket.getInputStream());
        this.dataOutput = new DataOutputStream(socket.getOutputStream());
    }

    /**
     * run executes whenever the thread starts. The server listens for incoming requests from the client
     * processes the requests and sends back a response
     */
    @Override
    public void run(){
        while (true){
            try{
                String serializedRequest = dataInput.readUTF();
                // Deserialize the request using Gson
                Request request = gson.fromJson(serializedRequest, Request.class);
                LOGGER.log(Level.INFO, "Client Request: " + currentUsername + " - " + request.getType());

                // Process the client request and obtain the response
                Response response = handleRequest(request);
                // Serialize the response and send it to the client using the output stream
                String serializedResponse = gson.toJson(response);
                dataOutput.writeUTF(serializedResponse);
                dataOutput.flush(); // Ensure the response is sent immediately
            } catch (EOFException e) {
                LOGGER.log(Level.INFO,"Server Info: Client Disconnected: " + currentUsername + " - " + socket.getRemoteSocketAddress());
                closeSocket();
                break;
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE,"Server Info: Client Connection Failed", e);
            }  catch (JsonSyntaxException e) {
                LOGGER.log(Level.SEVERE,"Server Info: Serialization Error", e);
            }
        }
    }
    /**
     * Handles incoming requests and returns response
     */
    public Response handleRequest(Request request) {
        switch (request.getType()){
            case REQUEST_MOVE:
                return handleRequestMove();
            case SEND_MOVE:
                int move = gson.fromJson(request.getData(), Integer.class);
                return handleSendMove(move);
            default:
                return new Response(Response.ResponseStatus.FAILURE, "Invalid Request");
        }
    }

    /**
     * Handles the SEND_MOVE request type
     */
    private Response handleSendMove(int move) {
        if(move < 0 || move > 8){ // Check for valid move
            return new Response(Response.ResponseStatus.FAILURE, "Invalid Move");
        }
        if(gameEvent.getTurn() == null || !gameEvent.getTurn().equals(currentUsername)) {
            // Save the move in the server and return a standard Response
            gameEvent.setMove(move);
            gameEvent.setTurn(currentUsername);
            return new Response(Response.ResponseStatus.SUCCESS, "Move Added");
        }else{
            return new Response(Response.ResponseStatus.FAILURE, "Not your turn to move");
        }
    }


    /**
     * Handles the REQUEST_MOVE request type
     */
    private GamingResponse handleRequestMove() {
        GamingResponse response = new GamingResponse();
        response.setStatus(Response.ResponseStatus.SUCCESS);

        if (gameEvent.getMove() != -1 && !gameEvent.getTurn().equals(currentUsername)){
            response.setMove(gameEvent.getMove());
            gameEvent.setMove(-1);
            gameEvent.setTurn(null);
        } else{
            response.setMove(-1);
        }
        return response;
    }

    /**
     * Closes all resources
     */
    private void closeSocket() {
        // Close socket connection and all IO streams
        try {
            socket.close();
            dataInput.close();
            dataOutput.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,"Server Info: Unable to close socket", e);
        }
    }
}