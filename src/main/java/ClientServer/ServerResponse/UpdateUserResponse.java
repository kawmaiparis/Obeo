package ClientServer.ServerResponse;

import user.ObeoUser;

public class UpdateUserResponse extends ServerResponse {
    private ObeoUser obeoUser;

    public UpdateUserResponse(ObeoUser obeoUser) {
        this.obeoUser = obeoUser;
    }

    public ObeoUser getObeoUser() {
        return obeoUser;
    }
}
