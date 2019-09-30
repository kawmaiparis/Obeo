package languages;

import database.ObeoConnection;
import database.RecordObjectHelper;
import user.ObeoUser;
import user.ObeoUserBuilder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserLanguageBuilder {

    public static List<UserLanguage> getLanguageFromId(long userId) {
        try {
            List<UserLanguage> userLanguages = new ArrayList<>();
            String s = "SELECT * FROM user_languages WHERE user_id = " + userId + ";";
            Statement stmt = ObeoConnection.getInstance().createStatement();
            ResultSet rs = stmt.executeQuery(s);

            while (rs.next()) {
                String language = rs.getString(2);
                int level = rs.getInt(3);
                userLanguages.add(new UserLanguage(userId, language, level));
            }
            return userLanguages;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static UserLanguage getSpecificUserLanguage(long userId, String searchingForLanguage) {
        try {
            String s = "SELECT * FROM user_languages WHERE user_id = " + userId + " "
                + "AND language = \'" + searchingForLanguage + "\';";
            Statement stmt = ObeoConnection.getInstance().createStatement();
            ResultSet rs = stmt.executeQuery(s);

            while (rs.next()) {
                String language = rs.getString(2);
                int level = rs.getInt(3);
                return new UserLanguage(userId, language, level);
            }
            System.out.println("User ID and language combination does not exist");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean addToDatabase(UserLanguage userLanguage)  {
        try {
            String s = "INSERT INTO user_languages (user_id, language, proficiency_level) "
                    + "VALUES (?, ?, ?)";
            PreparedStatement stmt = ObeoConnection.getInstance().prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, userLanguage.getUser_id());
            stmt.setString(2, userLanguage.getLanguage());
            stmt.setInt(3, userLanguage.getLevel());
            return RecordObjectHelper.addingDatabaseHelper(stmt, userLanguage);
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

    public static boolean updateInDatabase(UserLanguage userLanguage) {
        try {
            String s = "UPDATE user_languages SET proficiency_level = ? WHERE user_id = ? AND language = ?;";
            PreparedStatement stmt = ObeoConnection.getInstance().prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, userLanguage.getLevel());
            stmt.setLong(2, userLanguage.getUser_id());
            stmt.setString(3, userLanguage.getLanguage());


            boolean toRet = RecordObjectHelper.updateToDatabaseHelper(stmt);
            return toRet;

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

    public static boolean deleteLanguage(UserLanguage userLanguage) {
        try {
            String s = "DELETE from user_languages WHERE user_id = ? AND language = ?;";

            PreparedStatement stmt = ObeoConnection.getInstance().prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, userLanguage.getUser_id());
            stmt.setString(2, userLanguage.getLanguage());
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

    public static void deleteAllLanguagesForUser(long user_id) {
        UserLanguage dummyLanguage = new UserLanguage(user_id, null, 0);
        RecordObjectHelper.removeFromDatabase(dummyLanguage);
    }

    public static List<UserLanguage> getLanguagesForSpecificUserAndLevel(long userId, int level) {
        try {
            List<UserLanguage> userLanguages = new ArrayList<>();
            String s = "SELECT * FROM user_languages WHERE user_id = " + userId + " AND proficiency_level = " + level + ";";
            Statement stmt = ObeoConnection.getInstance().createStatement();
            ResultSet rs = stmt.executeQuery(s);

            while (rs.next()) {
                String language = rs.getString(2);
                int proficiency_level = rs.getInt(3);
                userLanguages.add(new UserLanguage(userId, language, proficiency_level));
            }
            return userLanguages;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
