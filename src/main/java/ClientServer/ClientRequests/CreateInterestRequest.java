package ClientServer.ClientRequests;

import interests.UserInterest;

public class CreateInterestRequest extends ClientRequest {
    private UserInterest userInterest;

    public CreateInterestRequest(UserInterest userInterest) {
        this.userInterest = userInterest;
    }

    public UserInterest getUserInterest() {
        return userInterest;
    }
}
