package ClientServer.ServerResponse;

public class MatchedUserResponse extends ServerResponse {

    private boolean isMatched;

    public MatchedUserResponse(boolean isMatched) {
        this.isMatched = isMatched;
    }

    public boolean isMatched() {
        return isMatched;
    }
}
