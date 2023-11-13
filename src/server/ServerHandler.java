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
import model.User;
import socket.PairingResponse;
import java.util.List;
import java.util.Map;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.DatabaseHelper;
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

    private int currentEventId;

    /**
     * ServerHandler constructor that accepts a Socket and username
     */
    public ServerHandler(Socket socket) throws IOException{
        LOGGER = Logger.getLogger(ServerHandler.class.getName());
        this.socket = socket;
        // Initialize the Gson object with a configuration that allows serializing null values
        this.gson = new GsonBuilder().serializeNulls().create();
        this.dataInput = new DataInputStream(socket.getInputStream());
        this.dataOutput = new DataOutputStream(socket.getOutputStream());
    }

    public void setUsername(String username){
        this.currentUsername = username;
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
            case LOGIN:
                return handleLogin(request);
            case REGISTER:
                return handleRegister(request);
            case UPDATE_PAIRING:
                return handleUpdatePairing();
            case SEND_INVITATION:
                String opponent = gson.fromJson(request.getData(), String.class);
                return handleSendInvitation(opponent);
            case ACCEPT_INVITATION:
                int eventId = gson.fromJson(request.getData(), Integer.class);
                return handleAcceptInvitation(eventId);
            case DECLINE_INVITATION:
                int eventId2 = gson.fromJson(request.getData(), Integer.class);
                return handleDeclineInvitation(eventId2);
            case ACKNOWLEDGE_RESPONSE:
                int eventId3 = gson.fromJson(request.getData(), Integer.class);
                return handleAcknowledgeResponse(eventId3);
            case ABORT_GAME:
                return handleAbortGame();
            case COMPLETE_GAME:
                return handleCompleteGame();
            default:
                return new Response(Response.ResponseStatus.FAILURE, "Invalid Request");
        }
    }

    /**
     * Handles the Complete game request
     */
    private Response handleCompleteGame(){
        Event gameEvent = DatabaseHelper.getInstance().getEvent(currentEventId);

        if(gameEvent == null || gameEvent.getStatus() != Event.EventStatus.PLAYING){
            return new Response(Response.ResponseStatus.FAILURE, "Invalid Complete game request");
        }

        gameEvent.setStatus(Event.EventStatus.COMPLETED);
        DatabaseHelper.getInstance().updateEvent(gameEvent);
        currentEventId = -1;

        return new Response(Response.ResponseStatus.SUCCESS, "Game completed successfully");
    }

    /**
     * Handles the Abort game request
     */
    private Response handleAbortGame(){
        Event gameEvent = DatabaseHelper.getInstance().getEvent(currentEventId);

        if(gameEvent == null || gameEvent.getStatus() != Event.EventStatus.PLAYING){
            return new Response(Response.ResponseStatus.FAILURE, "Invalid Abort Game request");
        }

        gameEvent.setStatus(Event.EventStatus.ABORTED);
        DatabaseHelper.getInstance().updateEvent(gameEvent);
        currentEventId = -1;

        return new Response(Response.ResponseStatus.SUCCESS,"Game aborted successfully");
    }



    /**
     * Handles the Acknowledge Response request
     */
    private Response handleAcknowledgeResponse(int eventId3){
        Event responseEvent = DatabaseHelper.getInstance().getEvent(eventId3);

        if(responseEvent == null || !responseEvent.getSender().equals(currentUsername)){
            return new Response(Response.ResponseStatus.FAILURE, "Invalid Acknowledgement");
        }

        if(responseEvent.getStatus() == Event.EventStatus.DECLINED){
            responseEvent.setStatus(Event.EventStatus.ABORTED);
        } else if (responseEvent.getStatus() == Event.EventStatus.ACCEPTED){
            currentEventId = eventId3;
            DatabaseHelper.getInstance().abortAllUserEvents(currentUsername);
        }

        DatabaseHelper.getInstance().updateEvent(responseEvent);

        return new Response(Response.ResponseStatus.SUCCESS, "Acknowledge Response handled successfully");
    }

    /**
     * Handles the Decline Invitation request
     */
    private Response handleDeclineInvitation(int eventId2){
        Event invitationEvent = DatabaseHelper.getInstance().getEvent(eventId2);

        if(invitationEvent == null || invitationEvent.getStatus() != Event.EventStatus.PENDING || !invitationEvent.getOpponent().equals(currentUsername)){
            return new Response(Response.ResponseStatus.FAILURE, "Invalid Invitation");
        }

        invitationEvent.setStatus(Event.EventStatus.DECLINED);

        DatabaseHelper.getInstance().updateEvent(invitationEvent);

        return new Response(Response.ResponseStatus.SUCCESS, "Invitation declined successfully");
    }

    /**
     * Handles the Accept Invitation request
     */
    private Response handleAcceptInvitation(int eventId){

        Event invitationEvent = DatabaseHelper.getInstance().getEvent(eventId);

        if(invitationEvent == null || invitationEvent.getStatus() != Event.EventStatus.PENDING || !invitationEvent.getOpponent().equals(currentUsername)){
            return new Response(Response.ResponseStatus.FAILURE, "Invalid invitation");
        }
        invitationEvent.setStatus(Event.EventStatus.ACCEPTED);

        DatabaseHelper.getInstance().abortAllUserEvents(currentUsername);

        DatabaseHelper.getInstance().updateEvent(invitationEvent);

        currentEventId = eventId;

        return new Response(Response.ResponseStatus.SUCCESS, "Invitation accepted");
    }

    /**
     * Handles the Send Invitation request
     */
    private Response handleSendInvitation(String opponent){

        if(currentUsername == null || currentUsername.isEmpty()){
            return new Response(Response.ResponseStatus.FAILURE, "User not logged in");
        }

        if(!DatabaseHelper.getInstance().isUserAvailable(opponent)){
            return new Response(Response.ResponseStatus.FAILURE, "Opponent is not available");
        }

        Event invitationEvent = new Event();
        invitationEvent.setSender(currentUsername);
        invitationEvent.setOpponent(opponent);
        invitationEvent.setStatus(Event.EventStatus.PENDING);
        invitationEvent.setMove(-1);

        DatabaseHelper.getInstance().createEvent(invitationEvent);

        return new Response(Response.ResponseStatus.SUCCESS, "Invitation sent successfully");
    }

    /**
     * Handles the Update pairing request
     */
    private PairingResponse handleUpdatePairing(){

        if(currentUsername == null || currentUsername.isEmpty()){
            return new PairingResponse(PairingResponse.PairingResponseStatus.FAILURE, "User not logged in", null, null);
        }

        List<User> availableUsers = DatabaseHelper.getInstance().getAvailableUsers();
        Map<String, Boolean> userInvitations = DatabaseHelper.getInstance().getUserInvitations(currentUsername);
        Map<String, Boolean> userInvitationResponses = DatabaseHelper.getInstance().getUserInvitationResponse(currentUsername);

        PairingResponse pairingResponse = new PairingResponse(PairingResponse.PairingResponseStatus.SUCCESS, "Update pairing successful", availableUsers, userInvitations, userInvitationResponses);

        return pairingResponse;
    }

    /**
     * Handles the Login request
     */
    private Response handleLogin(Request request){
        try{
            User loginUser = gson.fromJson(request.getData(), User.class);

            User existingUser = DatabaseHelper.getInstance().getUser(loginUser.getUsername());

            if(existingUser == null){
                return new Response(Response.ResponseStatus.FAILURE, "User not found");
            }

            if(!existingUser.getPassword().equals(loginUser.getPassword())){
                return new Response(Response.ResponseStatus.FAILURE, "Incorrect Password");
            }

            currentUsername = existingUser.getUsername();
            existingUser.setOnline(true);

            DatabaseHelper.getInstance().updateUser(existingUser);
            return new Response(Response.ResponseStatus.SUCCESS, "Login successful");
        } catch(JsonSyntaxException e){
            return new Response(Response.ResponseStatus.FAILURE, "Invalid login");
        }
    }

    /**
     * Handles the Register request
     */
    private Response handleRegister(Request request){
        try{
            User newUser = gson.fromJson(request.getData(), User.class);

            if(isUsernameExists(newUser.getUsername())){
                return new Response(Response.ResponseStatus.FAILURE, "Username already exists");
            }

            createUser(newUser);

            return new Response(Response.ResponseStatus.SUCCESS, "User registered successfully");
        } catch (JsonSyntaxException e){
            return new Response(Response.ResponseStatus.FAILURE, "Invalid username");
        }
    }

    /**
     * Checks to see if the username entered is already existing
     */
    private boolean isUsernameExists(String username){

        return false;
    }

    /**
     * Creates a user of type User
     */
    private void createUser(User user){

    }

    /**
     * Handles the SEND_MOVE request type
     */
    private Response handleSendMove(int move) {
        if(move < 0 || move > 8){ // Check for valid move
            return new Response(Response.ResponseStatus.FAILURE, "Invalid Move");
        }
        Event gameEvent = DatabaseHelper.getInstance().getEvent(currentEventId);

        if(gameEvent.getTurn() == null || !gameEvent.getTurn().equals(currentUsername)) {
            // Save the move in the server and return a standard Response
            gameEvent.setMove(move);
            gameEvent.setTurn(currentUsername);
            DatabaseHelper.getInstance().updateEvent(gameEvent);
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

        Event gameEvent = DatabaseHelper.getInstance().getEvent(currentEventId);

        if (gameEvent != null) {
            if(gameEvent.getStatus() == Event.EventStatus.ABORTED){
                response.setActive(false);
                response.setMessage("Opponent Abort");
            }else if(gameEvent.getStatus() == Event.EventStatus.COMPLETED){
                response.setActive(false);
                response.setMessage("Opponent denied Play Again");
            } else{
                response.setActive(true);
            }

            if (gameEvent.getMove() != -1 && !gameEvent.getTurn().equals(currentUsername)) {
                response.setMove(gameEvent.getMove());
                gameEvent.setMove(-1);
                gameEvent.setTurn(null);
                DatabaseHelper.getInstance().updateEvent(gameEvent);
            } else {
                response.setMove(-1);
            }
        }else {
            response.setStatus(Response.ResponseStatus.FAILURE);
            response.setMessage("Event not found");
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

            if(currentUsername != null){
                User user = DatabaseHelper.getInstance().getUser(currentUsername);

                user.setOnline(false);

                DatabaseHelper.getInstance().updateUser(user);

                DatabaseHelper.getInstance().abortAllUserEvents(currentUsername);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,"Server Info: Unable to close socket", e);
        }
    }
}