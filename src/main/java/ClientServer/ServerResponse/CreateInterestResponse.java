package ClientServer.ServerResponse;

import interests.UserInterest;

public class CreateInterestResponse extends ServerResponse {
    private UserInterest userInterest;

    public CreateInterestResponse(UserInterest userInterest) {
        this.userInterest = userInterest;
    }

    public UserInterest getUserInterest() {
        return userInterest;
    }
}
