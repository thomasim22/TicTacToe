package socket;
import java.lang.String;

public class Response {
    public ResponseStatus status;
    public String message;

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    enum ResponseStatus{
        SUCCESS,
        FAILURE
    }

    public Response(){
        this.status = ResponseStatus.FAILURE;
        this.message = "Request FAILURE";
    }

    public Response(ResponseStatus status, String message){
        this.status = status;
        this.message = message;
    }
}
