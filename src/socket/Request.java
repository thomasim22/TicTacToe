package socket;
/**
 * @author  thomasim22
 * @version Request v1
 */
public class Request {

    /**
     * @param type is a type of client request
     * @param data is a string representation of serialized data sent by the client. It can be a serialized object of a String, Integer, or User
     */
    private RequestType type;

    private String data;

    /**
     * @return the type for the certain RequestType
     */
    public RequestType getType() {
        return type;
    }

    /**
     * Sets the RequestType that was returned in the getType()
     */
    public void setType(RequestType type){
            this.type = type;
    }

    /**
     * @return the data gotten from the server or client
     */
    public String getData()
    {
        return data;
    }

    /**
     * Sets the data returned from the getData()
     */
    public void setData(String data){
        this.data = data;
    }

    /**
     * An enumeration for the different request types
     */
    enum RequestType{
        LOGIN, REGISTER, UPDATE_PAIRING, SEND_INVITATION, ACCEPT_INVITATION,
        DECLINE_INVITATION, ACKNOWLEDGE_RESPONSE, REQUEST_MOVE, SEND_MOVE,
        ABORT_GAME, COMPLETE_GAME
    }

    /**
     * Default constructor for the class
     */
    public Request(){
        this.type = RequestType.REGISTER;
        this.data = "";
    }

    /**
     * A constructor that sets the type and data
     */
    public Request(RequestType type, String data){
        this.type = type;
        this.data = data;
        }

}