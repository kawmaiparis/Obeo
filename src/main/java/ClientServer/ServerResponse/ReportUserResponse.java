package ClientServer.ServerResponse;

public class ReportUserResponse extends ServerResponse{
    private boolean isBlocked;

    public ReportUserResponse(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public boolean isBlocked() {
        return isBlocked;
    }
}
