package socket;
import java.util.List;
import model.Event;
import socket.GamingResponse;
import socket.Request;
import socket.Response;
import model.User;

public class PairingResponse extends Response {
    private List<User> availableUsers;
    private Event invitation;

    private Event invitationResponse;

    public UpdatePairingResponse(){
        super(ResponseStatus.SUCCESS, "default message");
    }
    public UpdatePairingResponse(String status, String message, List<User> availableUsers, Event invitation, Event invitationResponse){
        super(status, message);
        this.availableUsers = availableUsers;
        this.invitation = invitation;
        this.invitationResponse = invitationResponse;
    }

    public List<User> getAvailableUsers() {
        return availableUsers;
    }

    public void setAvailableUsers(List<User> availableUsers) {
        this.availableUsers = availableUsers;
    }

    public Event getInvitation() {
        return invitation;
    }

    public void setInvitation(Event invitation) {
        this.invitation = invitation;
    }

    public Event getInvitationResponse() {
        return invitationResponse;
    }

    public void setInvitationResponse(Event invitationResponse) {
        this.invitationResponse = invitationResponse;
    }

}
