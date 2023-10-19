package model;
import java.lang.String;

/**
 * @author  thomasim22 declaration
 *          kailisacco implementation
 * @version Event v1
 */
public class Event {

    /**
     * An enumeration type for the different game status
     */
    public enum EventStatus {
        PENDING, DECLINED, ACCEPTED, PLAYING, COMPLETED, ABORTED
    }

    /**
     * @param eventId is a global unique integer that represents an event
     * @param sender represents  the username of the user that sends the game invitation
     * @param opponent represents the username of the user that the game invitation was
     * sent to
     * @param status represents the status of a game; it is of type EventStatus
     * @param turn represents the username of the player that made the last move
     * @param move represents an integer storing the last move of the game
     */
    public int eventId;
    private String sender;
    private String opponent;
    private EventStatus status;
    private String turn;
    private int move;

    /**
     * Default constructor for the class
     */
    public Event() {
        this.eventId = eventId;
        this.sender = "Sender";
        this.opponent = "Opponent";
        this.status = status;
        this.turn = "Turn";
        this.move = move;
    }

    /**
     * A constructor that sets all the attributes of the class
     */
    public Event(int eventId, String sender, String opponent, EventStatus status, String turn, int move) {
        this.eventId = eventId;
        this.sender = sender;
        this.opponent = opponent;
        this.status = status;
        this.turn = turn;
        this.move = move;
    }

    /**
     * @return sender parameter
     */
    public String getSender() {
        return sender;
    }

    /**
     * Sets the sender value
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * @return the eventId
     */
    public int getEventID(){
        return eventId;
    }

    /**
     * Sets the eventId
     */
    public void setEventID(int eventId){
        this.eventId = eventId;
    }

    /**
     * @return opponent value
     */
    public String getOpponent() {
        return opponent;
    }

    /**
     * Sets the opponent value
     */
    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    /**
     * @return turn parameter
     */
    public String getTurn() {
        return turn;
    }

    /**
     * Sets the turn value
     */
    public void setTurn(String turn) {
        this.turn = turn;
    }


    /**
     * @return move parameter
     */
    public int getMove() {
        return move;
    }

    /**
     * Sets the move value
     */
    public void setMove(int move) {
        this.move = move;
    }


    /**
     * @return status parameter
     */
    public EventStatus getStatus() {
        return status;
    }

    /**
     * Sets the status
     */
    public void setStatus(EventStatus status) {
        this.status = status;}
    /**
     *

     * Overridden
     * @return if the values are equal to each other
     */
    public boolean equals(Object other){
        Event othereventId = (Event)other;
        return this.eventId == othereventId.eventId;
    }
}