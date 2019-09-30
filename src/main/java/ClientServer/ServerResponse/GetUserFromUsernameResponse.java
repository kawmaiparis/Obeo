package ClientServer.ServerResponse;

import user.ObeoUser;

public class GetUserFromUsernameResponse extends ServerResponse{
    private ObeoUser user;

    public GetUserFromUsernameResponse(ObeoUser user) {
        this.user = user;
    }

    public ObeoUser getUser() {
        return user;
    }
}
