package server;
/**
 * @author  thomasim22 declaration
 *          kailisacco implementation
 * @version ServerHandler v1
 */
import java.io.EOFException;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLOutput;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Event;
import socket.GamingResponse;
import socket.Request;
import socket.Response;
import static socket.Request.RequestType.REQUEST_MOVE;

/**
 * A class that helps SocketServer handle induvidual communications from users.
 */
public class ServerHandler extends Thread{

    /**
     * Socket in which the server communicates with the client
     */
    private Socket socket;

    /**
     * The username of the client
     */
    private String currentUsername;

    /**
     * Input stream to read client data
     */
    private DataInputStream dataInput;

    /**
     * Output stream to send client data
     */
    private DataOutputStream dataOutput;

    /**
     * Gson object to handle serialization and deserialization
     */
    private Gson gson;

    /**
     * Vairbale to store the game move and the player who made the move
     */
    private static Event gameEvent = new Event();

    /**
     * ServerHandler constructor that accepts a Socket and username
     */
    public ServerHandler(Socket socket, String username){
        this.socket = socket;
        this.currentUsername = username;
        try {
            // Initialize the data input and output streams
            this.dataInput = new DataInputStream(socket.getInputStream());
            this.dataOutput = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Error initializing data streams: " + e.getMessage());
        }

        // Initialize the Gson object with a configuration that allows serializing null values
        this.gson = new GsonBuilder().serializeNulls().create();
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
                System.out.println("Received request: " + serializedRequest);

                // Deserialize the request using Gson
                Request clientRequest = gson.fromJson(serializedRequest, Request.class);

                // Process the client request and obtain the response
                Response serverResponse = handleRequest(clientRequest);

                // Serialize the response and send it to the client using the output stream
                String serializedResponse = gson.toJson(serverResponse);
                System.out.println("Sending response: " + serializedResponse);
                dataOutput.writeUTF(serializedResponse);
                dataOutput.flush(); // Ensure the response is sent immediately

            } catch (EOFException eof) {
                // Client has disconnected, so break out of the loop and close resources
                System.out.println("Client " + currentUsername + " disconnected.");
                closeSocket();
                break;
            } catch (IOException e) {
                // Handle any other input/output errors
                System.err.println("Error processing request from client " + currentUsername + ": " + e.getMessage());
                closeSocket();
                break;
            }
        }

    }
    /**
     * Handles incoming requests and returns response
     */
    public Response handleRequest(Request request) {
        if (request.getType() == Request.RequestType.SEND_MOVE) {
            // Deserialize the move to class Integer
            System.out.println(request.getData());
            Integer move = new Gson().fromJson(request.getData(), Integer.class);
            return handleSendMove(move);
        } else if (request.getType() == Request.RequestType.REQUEST_MOVE) {
            return handleRequestMove();
        } else {
            return new Response(Response.ResponseStatus.FAILURE, "Invalid request type.");
        }
    }

    /**
     * Handles the SEND_MOVE request type
     */
    public Response handleSendMove(Integer move) {
        System.out.println("Handling SEND_MOVE for move: " + move + " by user: " + currentUsername);
        // Ensure the same user does not send consecutive moves
        if (gameEvent.getTurn() != null && gameEvent.getTurn().equals(currentUsername)) {
            return new Response(Response.ResponseStatus.FAILURE, "You can't make consecutive moves.");
        }

        // Set the move and turn in the gameEvent
        gameEvent.setMove(move);
        gameEvent.setTurn(currentUsername);
        System.out.println(gameEvent.getMove());
        System.out.println(gameEvent.getTurn());

        return new Response(Response.ResponseStatus.SUCCESS, "Move handled");
    }

    /**
     * Handles the REQUEST_MOVE request type
     */
    public GamingResponse handleRequestMove() {
        // Check if the last move was made by the opponent
        if (gameEvent.getTurn() != null && !gameEvent.getTurn().equals(currentUsername)) {
            int moveValue = gameEvent.getMove();
            gameEvent.setMove(-1); // Reset the move value after sending it to the opponent
            System.out.println("Sending back GamingResponse with move: " + moveValue);
            return new GamingResponse(moveValue, true); // Assuming opponent is active if there's a move
        } else {
            System.out.println("Sending back GamingResponse with move: -1");
            return new GamingResponse(-1, false); // No move and opponent not active
        }
    }

    /**
     * Closes all resources
     */
    public void closeSocket(){
        // Close the data streams and socket
        try {
            if (dataInput != null) {
                dataInput.close();
            }
            if (dataOutput != null) {
                dataOutput.close();
            }
            if (socket != null) {
                socket.close();
            }
            System.out.println("Successfully closed all resources for client: " + currentUsername);
        } catch (IOException e) {
            System.err.println("Error closing resources for client " + currentUsername + ": " + e.getMessage());
        }
    }
}