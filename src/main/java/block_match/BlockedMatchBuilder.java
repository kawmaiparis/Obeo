package block_match;

import database.ObeoConnection;
import database.RecordObjectHelper;
import interests.UserInterest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BlockedMatchBuilder {

    public static boolean deleteBlockedMatch(BlockedMatch blockedMatch) {
        try {
            String s = "DELETE from block_match WHERE user_one = ? AND user_two = ?;";

            PreparedStatement stmt = ObeoConnection.getInstance().prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, blockedMatch.getUserOneID());
            stmt.setLong(2, blockedMatch.getUserTwoID());
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

    public static boolean addBlockMatch(BlockedMatch blockedMatch) {
        try {
            String s = "INSERT INTO block_match (user_one, user_two, is_reported) "
                    + "VALUES (?, ?, ?)";
            PreparedStatement stmt = ObeoConnection.getInstance().prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, blockedMatch.getUserOneID());
            stmt.setLong(2, blockedMatch.getUserTwoID());
            stmt.setBoolean(3, blockedMatch.isReported());

            return RecordObjectHelper.addingDatabaseHelper(stmt, blockedMatch);
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

    public static boolean areTwoUsersBlocked(long userOneID, long userTwoID) {
        try {
            String s = "SELECT * FROM block_match WHERE user_one = " + userOneID+ " AND user_two = " + userTwoID
                    + " AND is_reported = true;";
            Statement stmt = ObeoConnection.getInstance().createStatement();
            ResultSet rs = stmt.executeQuery(s);
            boolean a = rs.next();
            String ss = "SELECT * FROM block_match WHERE user_one = " + userTwoID + " AND user_two = " + userOneID
                    + " AND is_reported = true;";
            Statement stmts = ObeoConnection.getInstance().createStatement();
            ResultSet rss = stmts.executeQuery(ss);
            return a || rss.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean areTwoUsersMatched(long userOneID, long userTwoID) {
        try {
            String s = "SELECT * FROM block_match WHERE user_one = " + userOneID+ " AND user_two = " + userTwoID
                    + " AND is_reported = false;";
            Statement stmt = ObeoConnection.getInstance().createStatement();
            ResultSet rs = stmt.executeQuery(s);
            boolean a = rs.next();
            String ss = "SELECT * FROM block_match WHERE user_one = " + userTwoID + " AND user_two = " + userOneID
                    + " AND is_reported = false;";
            Statement stmts = ObeoConnection.getInstance().createStatement();
            ResultSet rss = stmts.executeQuery(ss);
            return a || rss.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int numberOfReported(long userID) {
        try {
            String s = "SELECT * FROM block_match WHERE user_two = " + userID + " AND is_reported = " + true + ";";
            Statement stmt = ObeoConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(s);
            rs.last();
            return rs.getRow();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }
}
