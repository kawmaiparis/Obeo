package ClientServer.ClientRequests;

public class GetUserFromUsernameRequest extends ClientRequest {

    private String username;

    public GetUserFromUsernameRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
