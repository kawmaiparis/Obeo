package matching_users;

import user.ObeoUser;

import java.io.Serializable;

public class MatchedObeoUser implements Serializable, Comparable {
    public static double DISTANCE_MULTIPLIER;
    public static double INTEREST_MULTIPLIER;
    public static double AGE_MULTIPLIER;


    private ObeoUser local;
    private ObeoUser tourist;
    private double distance;
    private double score;

    public MatchedObeoUser(ObeoUser local, ObeoUser tourist, double distance, double score) {
        this.local = local;
        this.tourist = tourist;
        this.distance = distance;
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public ObeoUser getTourist() {
        return tourist;
    }

    public ObeoUser getLocal() {
        return local;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof MatchedObeoUser) {
            if (((MatchedObeoUser) o).getScore() < score) {
                return -1;
            } else {
                return 1;
            }
        } else{
            return 0;
        }
    }

}
