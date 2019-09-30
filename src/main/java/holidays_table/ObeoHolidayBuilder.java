package holidays_table;

import block_match.BlockedMatchBuilder;
import database.ObeoConnection;
import database.RecordObjectHelper;
import java.util.Collections;
import matching_users.MatchedObeoUser;
import matching_users.MatchingAlgorithm;
import user.ObeoUser;
import user.ObeoUserBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ObeoHolidayBuilder {


  public static ObeoHoliday buildHolidayFromId(long holiday_id) {
    try {
      String s = "SELECT * FROM holidays WHERE holiday_id = " + holiday_id + ";";
      Statement stmt = ObeoConnection.getInstance().createStatement();
      ResultSet rs = stmt.executeQuery(s);

      if (rs.next()) {
        return buildHolidayFromRS(rs);
      }
      System.out.println("Error: Query is empty!");
      return null;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static List<ObeoHoliday> getHolidaysForUserId(long userId) {
    try {
      List<ObeoHoliday> userHolidays = new ArrayList<>();
      String s = "SELECT * FROM holidays WHERE user_id = " + userId + ";";
      Statement stmt = ObeoConnection.getInstance().createStatement();
      ResultSet rs = stmt.executeQuery(s);

      while (rs.next()) {
        long holiday_id = rs.getLong(1);
        userHolidays.add(buildHolidayFromId(holiday_id));
      }
      return userHolidays;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }


  static ObeoHoliday buildHolidayFromRS(ResultSet rs) {
    try {
      long holiday_id = rs.getLong(1);
      long user_id = rs.getLong(2);
      Date a = rs.getDate(3);
      Date b = rs.getDate(4);
      String destination = rs.getString(5);
      double longitude = rs.getDouble(6);
      double latitude = rs.getDouble(7);
      long selection_Time = rs.getLong(9);

      return new ObeoHoliday(holiday_id, destination, a, b, user_id, longitude, latitude, selection_Time);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static boolean addToDatabase(ObeoHoliday obeoHoliday) {
    try {

      String s = "INSERT INTO holidays (destination_city, start_date, end_date, user_id, longitude, latitude, location_selection_time) " +
              "VALUES (?, ?, ?, ?, ?, ?, ?)";

      PreparedStatement stmt = ObeoConnection.getInstance().prepareStatement(s, Statement.RETURN_GENERATED_KEYS);

      stmt.setString(1, obeoHoliday.getDestination_city());
      stmt.setDate(2, obeoHoliday.getStart_date());
      stmt.setDate(3, obeoHoliday.getEnd_date());
      stmt.setLong(4,obeoHoliday.getUser_id());
      stmt.setDouble(5, obeoHoliday.getLongitude());
      stmt.setDouble(6, obeoHoliday.getLatitude());
      stmt.setLong(7, obeoHoliday.getSelection_time());
      RecordObjectHelper.addingDatabaseHelper(stmt, obeoHoliday);

      String p = "UPDATE holidays SET location = ST_POINT(longitude, latitude)";
      PreparedStatement stmtLocation = ObeoConnection.getInstance().prepareStatement(p);
      RecordObjectHelper.updateToDatabaseHelper(stmtLocation);

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

  public static boolean updateInDatabase(ObeoHoliday obeoHoliday) {
    try {
      String s = "UPDATE holidays SET destination_city = ?, start_date = ?, end_date = ?, user_id = ?, longitude = ?, latitude = ? WHERE holiday_id = ?";

      PreparedStatement stmt = ObeoConnection.getInstance().prepareStatement(s);
      stmt.setString(1, obeoHoliday.getDestination_city());
      stmt.setDate(2, obeoHoliday.getStart_date());
      stmt.setDate(3, obeoHoliday.getEnd_date());
      stmt.setLong(4, obeoHoliday.getUser_id());
      stmt.setLong(5, obeoHoliday.getId());
      stmt.setDouble(6, obeoHoliday.getLongitude());
      stmt.setDouble(7, obeoHoliday.getLatitude());
      return RecordObjectHelper.updateToDatabaseHelper(stmt);

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

  /*Get locals who live in holiday destination*/
  public static List<MatchedObeoUser> getLocals(ObeoHoliday holiday) {

    if (holiday == null) {
      System.out.println("Error: This holiday has not been assigned to database");
      return null;
    }

    ObeoUser tourist = ObeoUserBuilder.buildUserFromId(holiday.getUser_id());

    if (tourist == null) {
      System.out.println("Error: This user id has not been assigned to database");
      return null;
    }

    String city = holiday.getDestination_city();
    String s = "SELECT user_id FROM users WHERE home_city = \'" + city + "\';";
    try {
      Statement stmt = ObeoConnection.getInstance().createStatement();
      ResultSet rs = stmt.executeQuery(s);

      List<ObeoUser> locals = new ArrayList<>();
      while (rs.next()) {
        long currentID = rs.getLong(1);
        ObeoUser local = ObeoUserBuilder.buildUserFromId(currentID);
        if (local != null) {
          locals.add(local);
        }
      }

      return ObeoHolidayBuilder.holidayHelper(holiday, locals);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static List<MatchedObeoUser> holidayHelper(ObeoHoliday holiday, List<ObeoUser> locals) {
    List<MatchedObeoUser> matchedUsers = new ArrayList<>();
    ObeoUser tourist = ObeoUserBuilder.buildUserFromId(holiday.getUser_id());

    for (ObeoUser local: locals) {
      double distance = ObeoUserBuilder.findDistanceBetweenLocalAndHoliday(local.getId(), holiday.getId());
      if (distance == -1) {
        return null;
      }
      MatchedObeoUser matchedObeoUser = new MatchedObeoUser(local, tourist, distance,
          MatchingAlgorithm.getTotalScore(local, tourist, distance));

      assert (matchedObeoUser.getScore() == MatchingAlgorithm
          .getTotalScore(tourist, local, distance));


      if (MatchingAlgorithm.getLanguageScore(local, tourist) > 0
              && !BlockedMatchBuilder.areTwoUsersBlocked(tourist.getId(), local.getId())

              && !BlockedMatchBuilder.areTwoUsersMatched(local.getId(), tourist.getId())
              && !local.equals(tourist)) {


        matchedUsers.add(matchedObeoUser);
      }
    }

    Collections.sort(matchedUsers);
    return matchedUsers;
  }

  public static void deleteAllHolidaysForUser(long id) {
    List<ObeoHoliday> userHolidays = getHolidaysForUserId(id);
    for (ObeoHoliday h: userHolidays) {
      RecordObjectHelper.removeFromDatabase(h);
    }

  }
}
