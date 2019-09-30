package interests;

import database.RecordObject;

public class UserInterest extends RecordObject {
    private long user_id;
    private String interest;

    public UserInterest(long user_id, String interest) {
        super(user_id, "user_interests", "user_id");
        this.user_id = user_id;
        this.interest = interest;
    }



    public long getUser_id() {
        return user_id;
    }

    public String getInterest() {
        return interest;
    }

    @Override
    public String toString() {
        return "UserInterest{" +
                "user_id=" + user_id +
                ", interest='" + interest + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof UserInterest &&
                ((UserInterest) obj).user_id == this.user_id &&
                ((UserInterest) obj).interest.equals(this.interest));
    }
}
