package database;

import holidays_table.ObeoHolidayBuilder;
import interests.UserInterest;
import interests.UserInterestBuilder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import languages.UserLanguageBuilder;
import user.ObeoUser;

public class RecordObjectHelper {
    public static boolean addingDatabaseHelper(PreparedStatement stmt, RecordObject recordObject) {
        try {
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                System.out.println("Creating record failed, no rows affected.");
                ObeoConnection.getInstance().commit();
                return false;
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                     recordObject.setId(generatedKeys.getLong(1));
                } else {
                    System.out.println("Creating record failed, no ID obtained.");
                    ObeoConnection.getInstance().commit();
                    return false;
                }
            }


            ObeoConnection.getInstance().commit();
            stmt.close();

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

    public static boolean updateToDatabaseHelper(PreparedStatement stmt) {
        try {
            int affectedRows = stmt.executeUpdate();
            ObeoConnection.getInstance().commit();
            stmt.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                ObeoConnection.getInstance().commit();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return false;
        }
    }

    public static void removeFromDatabase(RecordObject recordObject) {
        try {
            if (recordObject instanceof ObeoUser) {
                UserLanguageBuilder.deleteAllLanguagesForUser(recordObject.getId());
                ObeoHolidayBuilder.deleteAllHolidaysForUser(recordObject.getId());
                UserInterestBuilder.deleteAllInterestsForUser(recordObject.getId());
            }
            String s = "DELETE from " + recordObject.getTableName() + " WHERE " + recordObject
                .getPrimaryKeyName() + " = " + recordObject.getId() + ";";
            Statement stmt = ObeoConnection.getInstance().createStatement();
            stmt.executeUpdate(s);
            ObeoConnection.getInstance().commit();
            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
