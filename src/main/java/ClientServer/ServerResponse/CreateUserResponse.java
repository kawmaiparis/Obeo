package ClientServer.ServerResponse;

import user.ObeoUser;

public class CreateUserResponse extends ServerResponse {

    private ObeoUser obeoUser;

    public CreateUserResponse(ObeoUser obeoUser) {
        this.obeoUser = obeoUser;
    }

    public ObeoUser getObeoUser() {
        return obeoUser;
    }
}
