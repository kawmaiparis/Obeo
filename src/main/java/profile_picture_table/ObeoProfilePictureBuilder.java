package profile_picture_table;

import database.ObeoConnection;

import database.RecordObjectHelper;
import java.sql.*;

public class ObeoProfilePictureBuilder {

  public static ObeoProfilePicture buildImageFromId(long user_id) throws Exception {
    String s = "SELECT * FROM profile_picture WHERE user_id = " + user_id + ";";
    Statement stmt = ObeoConnection.getInstance().createStatement();

    ResultSet rs = stmt.executeQuery(s);
    if (rs.next()) {
      return buildUserFromRS(rs);
    }
    return null;
  }

  public static ObeoProfilePicture buildUserFromRS(ResultSet rs) throws Exception {
    long id = rs.getLong(1);
    String image_url = rs.getString(2);
    return new ObeoProfilePicture(id, image_url);
  }

  public static boolean imageExists(long user_id) throws Exception {
    String s = "SELECT * FROM users WHERE user_id = " + user_id + ";";
    Statement stmt = ObeoConnection.getInstance().createStatement();
    ResultSet rs = stmt.executeQuery(s);
    boolean res = rs.next();
    stmt.close();
    return res;
  }

  public static boolean addToDatabase(ObeoProfilePicture obeoProfilePicture) throws Exception {
    try {
      String s =
              "INSERT INTO profile_picture (picture) "
                      + "VALUES (?)";

      PreparedStatement stmt = ObeoConnection.getInstance().prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
      stmt.setString(1, obeoProfilePicture.getImage_url());

      return RecordObjectHelper.addingDatabaseHelper(stmt, obeoProfilePicture);
    } catch (Exception e) {
      ObeoConnection.getInstance().commit();
      System.out.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }
  }

  public static boolean updateInDatabase(ObeoProfilePicture obeoProfilePicture) throws Exception {
    try {
      String s = "UPDATE profile_picture SET picture = ? WHERE user_id = ?";
      PreparedStatement stmt = ObeoConnection.getInstance().prepareStatement(s);
      stmt.setString(1, obeoProfilePicture.getImage_url());
      stmt.setLong(2, obeoProfilePicture.getId());

      return RecordObjectHelper.updateToDatabaseHelper(stmt);

    } catch (Exception e) {
      ObeoConnection.getInstance().commit();
      System.out.println(e.getClass().getName() + ": " + e.getMessage());
      return false;
    }
  }


  public static String getProfilePicture(long user_id) throws Exception {
    if (!imageExists(user_id)) {
      throw new SQLException("image does not exist");
    }
    String s = "SELECT * FROM profile_picture WHERE user_id = " + user_id + ";";
    Statement stmt = ObeoConnection.getInstance().createStatement();
    ResultSet rs = stmt.executeQuery(s);
    String picture_url = rs.getString(1);
    stmt.close();
    return picture_url;
  }
}
