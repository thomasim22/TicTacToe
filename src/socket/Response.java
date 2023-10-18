package socket;
import java.lang.String;

/**
 * @author  kailisacco
 * @version Response.java v1
 */
public class Response {
    public ResponseStatus status;
    public String message;

    /**
     * @return returns ResponseStatus type object indicative of the status of server response
     */
    public ResponseStatus getStatus() {
        return status;
    }

    /**
     * @param status The status of server response. It of type ResponseStatus
     */
    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    /**
     * @return returns string message description about the
     *         status of the client-server communication
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message assigns string message description about the
     *                status of the client-server communication.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    enum ResponseStatus{
        SUCCESS,
        FAILURE
    }

    /**
     *  default constructor for Response class
     */
    public Response(){
        this.status = ResponseStatus.FAILURE;
        this.message = "Request FAILURE";
    }

    /**
     * @param status    Of type ResponseStatus, either FAILURE or SUCCESS
     * @param message   String type message, representing the status of the response
     */
    public Response(ResponseStatus status, String message){
        this.status = status;
        this.message = message;
    }
}
