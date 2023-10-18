package socket;

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
        this.data = data;
        }

}