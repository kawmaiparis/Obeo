package ClientServer.ClientRequests;

public class ReportUserRequest extends ClientRequest {
    private long blockingUser;
    private long blockedUser;

    public ReportUserRequest(long blockingUser, long blockedUser) {
        this.blockingUser = blockingUser;
        this.blockedUser = blockedUser;
    }

    public long getBlockingUser() {
        return blockingUser;
    }

    public long getBlockedUser() {
        return blockedUser;
    }
}
