package socket;
import java.util.Random;
import socket.Response.ResponseStatus.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author  thomasim22 declaration
 *          kailisacco implementation
 * @version GamingResponse v1
 */
public class GamingResponse extends Response{
    private int move;
    private boolean active;

    /**
     * @param status    enumerated class type object, FAILURE or SUCCESS value. Attribute of Parent Class Response
     * @param message   string object typically indicative of the status. Attribute of Parent Class Response
     * @param move      integer value intended to be between 0 and 8 (inclusive)
     * @param active    boolean value to indicate if the opponent is active
     */
    public GamingResponse(ResponseStatus status, String message, int move, boolean active){
        super(status, message);
        this.move = move;
        this.active = active;
    }

    /**
     *  default constructor -- Failure, Failure message, random integer 0-8, false)
     *  accounts for super constructor call of failure status and a failure message.
     */
    public GamingResponse(){
        this(ResponseStatus.FAILURE, "Response FAILED", ThreadLocalRandom.current().nextInt(0,9), false);
    }

    /**
     * @return      integer return value between 0 and 8 (inclusive)
     */
    public int getMove() {
        return move;
    }

    /**
     * @param move  integer value between 0 and 8 (inclusive)
     */
    public void setMove(int move) {
        this.move = move;
    }

    /**
     * @return      boolean return value which tells if the opponent is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active boolean value which indicates whether the opponent is active or not
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
