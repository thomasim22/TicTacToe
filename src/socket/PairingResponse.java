package socket;

/**
 * @author  thomasim22 declaration/implementation
 * @version PairingResponse v1
 */

import model.Event;
import model.User;

import java.util.List;

/**
 * PairingResponse class for handling updates related to game pairing.
 */
public class PairingResponse extends Response {

    private List<User> availableUsers; // Players available for game invitations
    public Event invitation; // Invitation from another player
    public Event invitationResponse; // Response to an invitation sent by the user

    /**
     * Default constructor for PairingResponse that calls the constructor of the superclass
     */
    public PairingResponse() {
        super();
    }

    /**
     * Constructor that sets all attributes of the class that calls the constructor of the superclass
     *
     * @param status         The status of the response.
     * @param message        The message accompanying the response.
     * @param availableUsers The list of users available for invitations.
     * @param invitation     The game invitation event.
     * @param invitationResponse The response to the game invitation.
     */
    public PairingResponse(ResponseStatus status, String message, List<User> availableUsers, Event invitation, Event invitationResponse) {
        super(status, message);
        this.availableUsers = availableUsers;
        this.invitation = invitation;
        this.invitationResponse = invitationResponse;
    }


    /**
     * Getters and Setters for the parameters
     */
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