package ClientServer.ServerResponse;

import java.util.List;
import matching_users.MatchedObeoUser;

public class LookingForTouristsResponse extends ServerResponse {

    List<MatchedObeoUser> tourists;

    public LookingForTouristsResponse(List<MatchedObeoUser> tourists) {
        this.tourists = tourists;
    }

    public List<MatchedObeoUser> getTourists() {
        return tourists;
    }
}
