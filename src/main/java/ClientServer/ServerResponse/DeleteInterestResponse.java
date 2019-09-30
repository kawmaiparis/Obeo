package ClientServer.ServerResponse;

import interests.UserInterest;

public class DeleteInterestResponse extends ServerResponse {
    private UserInterest userInterest;

    public DeleteInterestResponse(UserInterest userInterest) {
        this.userInterest = userInterest;
    }

    public UserInterest getUserInterest() {
        return userInterest;
    }
}
