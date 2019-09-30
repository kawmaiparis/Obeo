package ClientServer.ClientRequests;

import user.ObeoUser;

public class LookingForTouristsRequest extends ClientRequest {
    private ObeoUser local;

    public LookingForTouristsRequest(ObeoUser local) {
        this.local = local;
    }

    public ObeoUser getLocal() {
        return local;
    }
}
