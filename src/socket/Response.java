package socket;
import java.lang.String;

public class Response {
    private ResponseStatus status;
    private String message;

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
        this.message = "Default Message";
    }

    public Response(ResponseStatus status, String message){
        this.status = status;
        this.message = message;
    }
}
