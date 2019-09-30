package interests;

import com.amazonaws.services.mq.model.User;
import database.ObeoConnection;
import database.RecordObjectHelper;
import holidays_table.ObeoHoliday;
import java.sql.SQLException;
import languages.UserLanguage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserInterestBuilder {

    public static List<UserInterest> getInterestsForUserid(long userId) {
        List<UserInterest> interestList = new ArrayList<>();
        try {
            String s = "SELECT * FROM user_interests WHERE user_id = " + userId + ";";
            Statement stmt = ObeoConnection.getInstance().createStatement();
            ResultSet rs = stmt.executeQuery(s);

            while (rs.next()) {
                String interest = rs.getString(2);
                interestList.add(new UserInterest(userId, interest));
            }

            return interestList;
        } catch (Exception e) {
            e.printStackTrace();
            return interestList;
        }
    }

    public static boolean addToDatabase(UserInterest userInterest) {
        try {
            String s = "INSERT INTO user_interests (user_id, interest) "
                    + "VALUES (?, ?)";
            PreparedStatement stmt = ObeoConnection.getInstance().prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, userInterest.getUser_id());
            stmt.setString(2, userInterest.getInterest());
            return RecordObjectHelper.addingDatabaseHelper(stmt, userInterest);
        } catch (Exception e) {
            try {
                ObeoConnection.getInstance().commit();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteInterest(UserInterest userInterest) {
        try {
            String s = "DELETE from user_interests WHERE user_id = ? AND interest = ?;";

            PreparedStatement stmt = ObeoConnection.getInstance().prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, userInterest.getUser_id());
            stmt.setString(2, userInterest.getInterest());
            stmt.executeUpdate();
            ObeoConnection.getInstance().commit();
            return true;

        } catch (Exception e) {
            try {
                ObeoConnection.getInstance().commit();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    public static void deleteAllInterestsForUser(long id) {
        List<UserInterest> userInterests = getInterestsForUserid(id);
        for (UserInterest i: userInterests) {
            RecordObjectHelper.removeFromDatabase(i);
        }

    }
}
