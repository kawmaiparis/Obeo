package ClientServer.ServerResponse;

import java.util.List;
import matching_users.MatchedObeoUser;

public class LookingForLocalsResponse extends ServerResponse {

    private List<MatchedObeoUser> locals;

    public LookingForLocalsResponse(List<MatchedObeoUser> locals) {
        this.locals = locals;
    }

    public List<MatchedObeoUser> getLocals() {
        return locals;
    }
}
