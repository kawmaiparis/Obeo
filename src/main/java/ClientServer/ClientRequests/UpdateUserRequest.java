package ClientServer.ClientRequests;

import user.ObeoUser;

public class UpdateUserRequest extends ClientRequest {

    private ObeoUser obeoUser;

    public UpdateUserRequest(ObeoUser obeoUser) {
        this.obeoUser = obeoUser;
    }

    public ObeoUser getObeoUser() {
        return obeoUser;
    }
}
