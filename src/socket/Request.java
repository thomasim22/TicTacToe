package socket;
import java.lang.String;

public class Request {

    private RequestType type;

    private String data;

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type){
            this.type = type;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data){
        this.data = data;
    }

    enum RequestType{
        LOGIN, REGISTER, UPDATE_PAIRING, SEND_INVITATION, ACCEPT_INVITATION,
        DECLINE_INVITATION, ACKNOWLEDGE_RESPONSE, REQUEST_MOVE, SEND_MOVE,
        ABORT_GAME, COMPLETE_GAME
    }

    public Request(){
        this.type = RequestType.REGISTER;
        this.data = "Register Please";
    }

    public Request(RequestType type, String data){
        this.type = type;
        switch(type){
            case LOGIN:
                this.data = data;
                break;
            case UPDATE_PAIRING:
                this.data = null;
                break;
            case SEND_INVITATION:
                this.data = data;
                break;
            case ACCEPT_INVITATION:
                this.data = data;
                break;
            case DECLINE_INVITATION:
                this.data = data;
                break;
            case ACKNOWLEDGE_RESPONSE:
                this.data = data;
                break;
            case REQUEST_MOVE:
                this.data = null;
                break;
            case SEND_MOVE:
                this.data = data;
                break;
            case ABORT_GAME:
                this.data = null;
                break;
            case COMPLETE_GAME:
                this.data = null;
                break;
            default:
                this.data = "Register Please";
                break;
        }

    }

}
