package server;

/**
 * @author  thomasim22 declaration/implementation
 * @version ServerHandler v1
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import model.Event;
import model.User;
import socket.GamingResponse;
import socket.PairingResponse;
import socket.Request;
import socket.Response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Handles individual user communications
 */
public class ServerHandler extends Thread {

    /**
     * Used for printing logs of different levels
     */
    private final Logger LOGGER;

    /**
     * Socket connection with a client
     */
    private final Socket socket;

    /**
     * User variable
     */
    private User user;

    /**
     * databaseHelper object to handle database functions
     */
    private DatabaseHelper databaseHelper;

    /**
     * Input stream to get clients response
     */
    private final DataInputStream inputStream;

    /**
     * Output Stream to send client a response
     */
    private final DataOutputStream outputStream;

    /**
     * Used to serialize/deserialize objects
     */
    private final Gson gson;

    /**
     * Integer to represent the current event happening
     */
    private int currentEventId;

    /**
     * Username of the current client of this socket connection
     */
    public String currentUsername;

    /**
     * Default constructor that initializes all attributes
     * @param socket Client's socket connection after server accepts
     * @throws IOException When no valid input or output stream from socket
     */
    public ServerHandler(Socket socket) throws IOException {
        LOGGER = Logger.getLogger(ServerHandler.class.getName());
        this.databaseHelper = DatabaseHelper.getInstance();

        this.socket = socket;
        this.gson = new GsonBuilder().serializeNulls().create();
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
    }

    /**
     * The function continuously runs and waits for a client request and sends a response
     */
    @Override
    public void run() {
        // Keep accepting request until client disconnects or send invalid request
        while (true) {
            try {
                String serializedRequest = inputStream.readUTF();
                Request request = gson.fromJson(serializedRequest, Request.class);
                LOGGER.log(Level.INFO,"Client Request: " + currentUsername + " - " + request.getType());

                Response response = handleRequest(request);
                String serializedResponse = gson.toJson(response);
                outputStream.writeUTF(serializedResponse);
                outputStream.flush();
            } catch (EOFException e) {
                LOGGER.log(Level.INFO,"Server Info: Client Disconnected: " + currentUsername + " - " + socket.getRemoteSocketAddress());
                closeSocket();
                break;
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE,"Server Info: Client Connection Failed", e);
            } catch (JsonSyntaxException e) {
                LOGGER.log(Level.SEVERE,"Server Info: Serialization Error", e);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE,"Server Info: Serialization Error", e);
            }
        }
    }

    /**
     * Closes client's connection and handles user offline status and event abortion
     */
    private void closeSocket() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }

            LOGGER.log(Level.INFO, "Server Info: Socket and IO streams closed successfully.");

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Server Info: Unable to close socket", e);
        }


        try {
            if (currentUsername != null) {
                User user = databaseHelper.getUser(currentUsername);
                user.setOnline(false);
                databaseHelper.updateUser(user);
                databaseHelper.abortAllUserEvents(currentUsername);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Server Info: Error handling user offline status and event abortion", e);
        }
    }


    /**
     * Handles all clients RequestType
     * @param request The request to handle
     * @return Response to client's request
     */
    public Response handleRequest(Request request) throws SQLException {
        int eventId;
        switch (request.getType()) {
            case LOGIN:
                user = new Gson().fromJson(request.getData(), User.class);
                return handleLogin(user);
            case REGISTER:
                user = new Gson().fromJson(request.getData(), User.class);
                return handleRegister(user);
            case UPDATE_PAIRING:
                return handleUpdatePairing();
            case SEND_INVITATION:
                String opponent = new Gson().fromJson(request.getData(), String.class);
                return handleSendInvitation(opponent);
            case ACCEPT_INVITATION:
                eventId = new Gson().fromJson(request.getData(), Integer.class);
                return handleAcceptInvitation(eventId);
            case DECLINE_INVITATION:
                eventId = new Gson().fromJson(request.getData(), Integer.class);
                return handleDeclineInvitation(eventId);
            case ACKNOWLEDGE_RESPONSE:
                eventId = new Gson().fromJson(request.getData(), Integer.class);
                return handleAcknowledgeResponse(eventId);
            case ABORT_GAME:
                return handleAbortGame();
            case COMPLETE_GAME:
                return handleCompleteGame();
            case REQUEST_MOVE:
                return handleRequestMove();
            case SEND_MOVE:
                Integer move = new Gson().fromJson(request.getData(), Integer.class);
                return handleSendMove(move);
            default:
                return new Response(Response.ResponseStatus.FAILURE, "Invalid request type.");
        }
    }


    /**
     * Handle request of type REQUEST_MOVE
     * @return a game response with move information
     */
    private GamingResponse handleRequestMove() throws SQLException {
        GamingResponse response = new GamingResponse();
        response.setStatus(Response.ResponseStatus.SUCCESS);
        Event event = databaseHelper.getEvent(currentEventId);

        if (event.getMove() != -1 && !event.getTurn().equals(currentUsername)){
            response.setMove(event.getMove());
            event.setMove(-1);
            event.setTurn(null);
        } else {
            response.setMove(-1);
        }
        return response;
    }

    /**
     * Handle request of type SEND_MOVE
     * @param move the move to be added to the game
     * @return a standard response
     */
    private Response handleSendMove(int move) throws SQLException {
        if(move < 0 || move > 8){ // Check for valid move
            return new Response(Response.ResponseStatus.FAILURE, "Invalid Move");
        }
        Event event = databaseHelper.getEvent(currentEventId);
        if(event.getTurn() == null || !event.getTurn().equals(currentUsername)) {
            event.setMove(move);
            event.setTurn(currentUsername);
            return new Response(Response.ResponseStatus.SUCCESS, "Move Added");
        }else{
            return new Response(Response.ResponseStatus.FAILURE, "Not your turn to move");
        }
    }

    /**
     * Processes a user registration request by checking if the username already exists and
     * creating a new user in the database if the user does not exist
     * @param user The User object containing the user's registration information
     * @return A Response object indicating the result of the registration attempt
     */
    public Response handleRegister(User user) throws SQLException {

        if (databaseHelper.isUsernameExists(user.getUsername())) {
            return new Response(Response.ResponseStatus.FAILURE, "Username already exists.");
        } else {

            databaseHelper.createUser(user);
            return new Response(Response.ResponseStatus.SUCCESS, "Registration successful.");
        }
    }

    /**
     * Processes a user login request by authenticating the user with the database and
     * setting the user to online
     * @param user The User object containing the user's login credentials
     * @return A Response object indicating the result of the login attempt
     */
    public Response handleLogin(User user) throws SQLException {

        User storedUser = databaseHelper.getUser(user.getUsername());

        if (storedUser != null && storedUser.getPassword().equals(user.getPassword())) {
            currentUsername = storedUser.getUsername();

            storedUser.setOnline(true);

            databaseHelper.updateUser(storedUser);

            return new Response(Response.ResponseStatus.SUCCESS, "Login successful.");
        } else {
            return new Response(Response.ResponseStatus.FAILURE, "Invalid username or password.");
        }
    }

    /**
     * Provides an update of the pairing status
     * @return A PairingResponse object containing the current pairing status.
     */
    public PairingResponse handleUpdatePairing() {
        if (currentUsername == null || currentUsername.isEmpty()) {
            return new PairingResponse(Response.ResponseStatus.FAILURE, "User not logged in.", null, null, null);
        }

        try {
            List<User> availableUsers = databaseHelper.getAvailableUsers(currentUsername);
            Event invitation = databaseHelper.getUserInvitation(currentUsername);
            Event invitationResponse = databaseHelper.getUserInvitationResponse(currentUsername);

            return new PairingResponse(Response.ResponseStatus.SUCCESS, "Pairing update retrieved successfully.", availableUsers, invitation, invitationResponse);

        } catch (SQLException e) {
            e.printStackTrace();
            return new PairingResponse(Response.ResponseStatus.FAILURE, "Failed to retrieve pairing updates.", null, null, null);
        }
    }

    /**
     * Sends an invitation to the specified opponent to start a new event and checks to see if the use is logged in and online
     * @param opponent The username of the opponent to send an invitation to
     * @return A Response object indicating the result of the invitation attempt
     */
    public Response handleSendInvitation(String opponent) throws SQLException {

        if (currentUsername == null || currentUsername.isEmpty()) {
            return new Response(Response.ResponseStatus.FAILURE, "User not logged in.");
        }

        if (!databaseHelper.isUserAvailable(opponent)) {
            return new Response(Response.ResponseStatus.FAILURE, "Opponent is currently not available.");
        }

        Event event = new Event(-1, currentUsername, opponent, Event.EventStatus.PENDING, currentUsername, -1);

        databaseHelper.createEvent(event);

        return new Response(Response.ResponseStatus.SUCCESS, "Invitation sent successfully to " + opponent + ".");
    }

    /**
     * Handles the acceptance of an invitation by updating the event status to ACCEPTED
     * It verifies if the event is pending and if the current user is the opponent
     * @param eventId The unique identifier of the event to accept.
     * @return A Response object indicating the result of the accept attempt.
     */
    public Response handleAcceptInvitation(int eventId) throws SQLException {
        Event event = databaseHelper.getEvent(eventId);

        if (event != null && event.getStatus() == Event.EventStatus.PENDING && event.getOpponent().equals(currentUsername)) {
            event.setStatus(Event.EventStatus.ACCEPTED);
            databaseHelper.abortAllUserEvents(currentUsername);
            databaseHelper.updateEvent(event);
            currentEventId = eventId;
            return new Response(Response.ResponseStatus.SUCCESS, "Invitation accepted successfully.");
        } else {
            return new Response(Response.ResponseStatus.FAILURE, "Event doesn't exist or isn't pending or user isn't opponent");
        }
    }

    /**
     * Handles the decline of an invitation by updating the event status to DECLINED
     * It checks if the event is pending and if the current user is the opponent
     * @param eventId The unique identifier of the event to decline
     * @return A Response object indicating the result of the decline attempt
     */
    public Response handleDeclineInvitation(int eventId) throws SQLException {
        Event event = databaseHelper.getEvent(eventId);

        if (event != null && event.getStatus() == Event.EventStatus.PENDING && event.getOpponent().equals(currentUsername)) {
            event.setStatus(Event.EventStatus.DECLINED);
            databaseHelper.updateEvent(event);
            return new Response(Response.ResponseStatus.SUCCESS, "Invitation declined successfully.");
        } else {
            return new Response(Response.ResponseStatus.FAILURE, "Event doesn't exist or isn't pending or user isn't opponent");
        }
    }

    /**
     * Acknowledges the response of an event invitation by setting the event status to that specific status
     * @param eventId The unique identifier of the event to acknowledge
     * @return A Response object indicating the result of the acknowledgment
     */
    public Response handleAcknowledgeResponse(int eventId) throws SQLException {
        Event event = databaseHelper.getEvent(eventId);

        if (event != null && event.getOpponent().equals(currentUsername)) {
            if (event.getStatus() == Event.EventStatus.ACCEPTED) {
                currentEventId = eventId;
                databaseHelper.abortAllUserEvents(currentUsername);
            } else if (event.getStatus() == Event.EventStatus.DECLINED) {
                event.setStatus(Event.EventStatus.ABORTED);
            }

            databaseHelper.updateEvent(event);
            return new Response(Response.ResponseStatus.SUCCESS, "Acknowledged response successfully.");
        } else {
            return new Response(Response.ResponseStatus.FAILURE, "Event doesn't exist or user isn't opponent");
        }
    }

    /**
     * Completes a game by updating the event status to COMPLETED
     * It checks if the event is in PLAYING status and updates it
     * @return A Response object indicating the result of the completion attempt
     */
    public Response handleCompleteGame() throws SQLException {
        Event event = databaseHelper.getEvent(currentEventId);

        if (event != null && event.getStatus() == Event.EventStatus.PLAYING) {
            event.setStatus(Event.EventStatus.COMPLETED);
            databaseHelper.updateEvent(event);
            currentEventId = -1;
            return new Response(Response.ResponseStatus.SUCCESS, "Game completed successfully.");
        } else {
            return new Response(Response.ResponseStatus.FAILURE, "Event doesn't exist or status isn't PLAYING");
        }
    }

    /**
     * Aborts a game by updating the event status to ABORTED
     * It checks that the event is in PLAYING status before aborting
     * @return A Response object indicating the result of the abort attempt
     */
    public Response handleAbortGame() throws SQLException {
        Event event = databaseHelper.getEvent(currentEventId);

        if (event != null && event.getStatus() == Event.EventStatus.PLAYING) {
            event.setStatus(Event.EventStatus.ABORTED);
            databaseHelper.updateEvent(event);
            currentEventId = -1;
            return new Response(Response.ResponseStatus.SUCCESS, "Game aborted successfully.");
        } else {
            return new Response(Response.ResponseStatus.FAILURE, "Event doesn't exist or status isn't PLAYING");
        }
    }
}