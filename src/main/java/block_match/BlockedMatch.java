package block_match;

import database.RecordObject;

public class BlockedMatch extends RecordObject {

    private long userOneID;
    private long userTwoID;
    private boolean isReported;

    public BlockedMatch(long userOneID, long userTwoID, boolean isReported) {
        super(userOneID, "block_match", "user_one");
        this.userOneID = userOneID;
        this.userTwoID = userTwoID;
        this.isReported = isReported;
    }

    public long getUserOneID() {
        return userOneID;
    }

    public long getUserTwoID() {
        return userTwoID;
    }

    public boolean isReported() {
        return isReported;
    }
}
