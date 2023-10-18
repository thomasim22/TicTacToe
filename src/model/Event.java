package model;
import java.lang.String;
public class Event {

    /**
     * An enumeration of the different game statuses
     */
    public enum EventStatus {
        PENDING, DECLINED, ACCEPTED, PLAYING, COMPLETED, ABORTED
    }

    /**
     * @param eventId is a global unique integer to represent an event
     * @param sender represents the username of the user sending game invitations
     * @param opponent represents the username of the user the invitation was sent to
     * @param status represents status of the game; it is a type EventStatus
     * @param turn represents the username of the player who made the last move
     * @param move represents an integer storing the last move of the game
     *
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
     * Sets all the attributes of the class
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
     * @return the sender parameter
     */
    public String getSender() {
        return sender;
    }

    /**
     * Sets the sender to the value returned from getSender()
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * @return opponent parameter
     */
    public String getOpponent() {
        return opponent;
    }

    /**
     * Sets the opponent value to the value returned from getOpponent()
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
     * Sets turn value to the value returned from the getTurn()
     */
    public void setTurn(String turn) {
        this.turn = turn;
    }

    public boolean equals(Object other) {
        Event othereventId = (Event) other;
        return this.eventId.equals(othereventId.eventId) &&
                this.eventId == othereventId.eventId;
    }
}