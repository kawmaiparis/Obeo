package ClientServer.ClientRequests;

public class MatchedUserRequest extends ClientRequest {
    private long userOneID;
    private long userTwoID;

    public MatchedUserRequest(long userOneID, long userTwoID) {
        this.userOneID = userOneID;
        this.userTwoID = userTwoID;
    }

    public long getUserOneID() {
        return userOneID;
    }

    public long getUserTwoID() {
        return userTwoID;
    }
}
