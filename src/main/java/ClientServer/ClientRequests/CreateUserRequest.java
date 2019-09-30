package ClientServer.ClientRequests;

import user.ObeoUser;

public class CreateUserRequest extends ClientRequest {

    private ObeoUser obeoUser;

    public CreateUserRequest(ObeoUser obeoUser) {
        assert (obeoUser.getId() == -1);
        this.obeoUser = obeoUser;
    }

    public ObeoUser getObeoUser() {
        return obeoUser;
    }
}
