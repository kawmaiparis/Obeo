package ClientServer.ClientRequests;

import interests.UserInterest;

public class DeleteInterestRequest extends ClientRequest {
    private UserInterest userInterest;

    public DeleteInterestRequest(UserInterest userInterest) {
        this.userInterest = userInterest;
    }

    public UserInterest getUserInterest() {
        return userInterest;
    }
}
