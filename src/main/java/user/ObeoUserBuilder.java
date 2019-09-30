package user;


import MesiboMessenger.AuthToken;
import block_match.BlockedMatch;
import block_match.BlockedMatchBuilder;
import database.ObeoConnection;
import database.RecordObjectHelper;
import holidays_table.ObeoHoliday;
import holidays_table.ObeoHolidayBuilder;
import interests.UserInterest;
import interests.UserInterestBuilder;
import languages.UserLanguage;
import languages.UserLanguageBuilder;
import matching_users.MatchedObeoUser;
import matching_users.MatchingAlgorithm;
import profile_picture_table.BucketPicture;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ObeoUserBuilder {
    private static List<String> reserved = new ArrayList<>();

    static {
        reserved.add("samantha");
        reserved.add("paris");
        reserved.add("rewaj");
        reserved.add("avish");
        reserved.add("rohan");
        reserved.add("prim");
    }

    public ObeoUserBuilder() {
    }

    public static List<String> getReserved() {
        return reserved;
    }

    public static ObeoUser buildUserFromId(long user_id) {
        String s = "SELECT * FROM users WHERE user_id = " + user_id + ";";
        Statement stmt = null;
        try {
            stmt = ObeoConnection.getInstance().createStatement();
            ResultSet rs = stmt.executeQuery(s);
            if (rs.next()) {
                return buildUserFromRS(rs);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    public static ObeoUser buildUserFromRS(ResultSet rs) {
        try {
            long id = rs.getLong(1);
            String user_name = rs.getString(2);
            String first_name = rs.getString(3);
            String second_name = rs.getString(4);
            Date date_of_birth = rs.getDate(5);
            String home_cty = rs.getString(6);
            double longitude = rs.getDouble(8);
            double latitude = rs.getLong(9);
            String password = rs.getString(10);
            boolean profile_picture_exists = rs.getBoolean(11);
            boolean isMale = rs.getBoolean(12);
            String bio = rs.getString(13);
            String messenger_token = rs.getString(14);
            byte[] image = BucketPicture.bytesFromBucket(id, profile_picture_exists);
//            File image = null;
            List<ObeoHoliday> userHolidays = ObeoHolidayBuilder.getHolidaysForUserId(id);
            List<UserInterest> userInterests = UserInterestBuilder.getInterestsForUserid(id);
            List<UserLanguage> userLanguages = UserLanguageBuilder.getLanguageFromId(id);
//            if (image == null) {
//                return null;
//            }

            ObeoUser toRet = new ObeoUser(id, user_name, first_name, second_name, date_of_birth, home_cty,
                longitude, latitude, image, password, userHolidays, userLanguages, userInterests, false, isMale, bio, messenger_token);
            toRet.setAge(getAge(toRet));
            return toRet;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static ObeoUser buildUserFromUsernameAndPassword(String username, String userpassword) {
        String s = "SELECT * FROM users WHERE user_name = \'" + username + "\' AND user_password = \'" + userpassword + "\';";

        try {

            Statement stmt = ObeoConnection.getInstance().createStatement();

            ResultSet rs = stmt.executeQuery(s);
            if (rs.next()) {
                return buildUserFromRS(rs);
            }
            System.out.println("Error: no user with given username and password");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ObeoUser buildUserFromUsername(String username) {
        String s = "SELECT * FROM users WHERE user_name = \'" + username + "\';";


        try {

            Statement stmt = ObeoConnection.getInstance().createStatement();

            ResultSet rs = stmt.executeQuery(s);
            if (rs.next()) {
                return buildUserFromRS(rs);
            }
            System.out.println("Error: no user with given username ");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static double findDistanceBetween(long userOne, long userTwo) {
        String s ="SELECT ST_Distance_Sphere(geometry(a.location), geometry(b.location)) " +
                "FROM users a, users b " +
                "WHERE a.user_id = " + userOne + " AND b.user_id = " + userTwo + ";";
        Statement stmt = null;
        try {
            stmt = ObeoConnection.getInstance().createStatement();
            ResultSet rs = stmt.executeQuery(s);
            if (rs.next()) {
                return rs.getDouble(1)/1609.34;
            }
            System.out.println("Error: Query is empty");
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

    }

    public static double findDistanceBetweenLocalAndHoliday(long local, long holidayId) {
        String s ="SELECT ST_Distance_Sphere(geometry(a.location), geometry(b.location)) " +
            "FROM users a, holidays b " +
            "WHERE a.user_id = " + local + " AND b.holiday_id = " + holidayId + ";";
        Statement stmt;
        try {
            stmt = ObeoConnection.getInstance().createStatement();
            ResultSet rs = stmt.executeQuery(s);
            if (rs.next()) {
                return rs.getDouble(1)/1609.34;
            }
            System.out.println("Error: Query is empty");
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

    }


    public static long getAge(ObeoUser obeoUser) {
        LocalDate localDate = LocalDate.now();
        LocalDate date_of_birth = obeoUser.getDate_of_birth().toLocalDate();
        return date_of_birth.until(localDate, ChronoUnit.YEARS);
    }

    public static boolean userExists(long user_id) {
        try {
            String s = "SELECT * FROM users WHERE user_id = " + user_id + ";";
            Statement stmt = ObeoConnection.getInstance().createStatement();
            ResultSet rs = stmt.executeQuery(s);
            boolean res = rs.next();
            stmt.close();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean apiAddNewUser(ObeoUser obeoUser) {
        if (!reserved.contains(obeoUser.getUser_name())) {
            return addToDatabase(obeoUser);

        }
        return false;
    }

    static public boolean addToDatabase(ObeoUser obeoUser) {
        String t = AuthToken.generateAuthToken(obeoUser.getUser_name());
//
        obeoUser.setMessenger_token(t);
        try {

            String s =
                    "INSERT INTO users (user_name, first_name, second_name, date_of_birth, home_city, "
                        + "longitude, latitude, user_password, profile_picture, is_male, description, token) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


            PreparedStatement stmt = ObeoConnection.getInstance().prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
            if (!setStmtMembers(stmt, obeoUser)) {
                return false;
            }

            boolean added = RecordObjectHelper.addingDatabaseHelper(stmt, obeoUser);
            obeoUser.setAge(getAge(obeoUser));

            obeoUser.setImage_exists(BucketPicture.storeImage(obeoUser.getId(), obeoUser.getProfile_bits()));
            String p = "UPDATE users SET location = ST_POINT(longitude, latitude);";
            PreparedStatement statementp = ObeoConnection.getInstance().prepareStatement(p);
            RecordObjectHelper.updateToDatabaseHelper(statementp);

            if (obeoUser.isImage_exists()) {
                s = "UPDATE users SET profile_picture = ? WHERE user_id = ?";
                PreparedStatement statement = ObeoConnection.getInstance().prepareStatement(s);
                statement.setBoolean(1, true);
                statement.setLong(2, obeoUser.getId());
                return RecordObjectHelper.updateToDatabaseHelper(statement);

            }
            obeoUser.setProfile_bits(BucketPicture.bytesFromBucket(obeoUser.getId(), false));
            return added;
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

    public static boolean updateInDatabase(ObeoUser obeoUser) {
        try {



            BucketPicture.storeImage(obeoUser.getId(), obeoUser.getProfile_bits());
            obeoUser.setImage_exists(true);

            String s = "UPDATE  users SET  first_name = ?, second_name = ?, home_city = ?, longitude = ?, "
                + "latitude = ?, user_password = ?, profile_picture = ?, is_male = ?, description = ?" +
                    "WHERE user_id = ?;";
            PreparedStatement stmt = ObeoConnection.getInstance().prepareStatement(s);
            stmt.setString(1, obeoUser.getFirst_name());
            stmt.setString(2, obeoUser.getSecond_name());
            stmt.setString(3, obeoUser.getHome_city());
            stmt.setDouble(4, obeoUser.getLongitude());
            stmt.setDouble(5, obeoUser.getLatitude());
            stmt.setString(6, obeoUser.getUser_password());
            stmt.setBoolean(7, obeoUser.isImage_exists());
            stmt.setBoolean(8, obeoUser.isMale());
            stmt.setString(9, obeoUser.getBio());
            stmt.setLong(10, obeoUser.getId());

            boolean toRet = RecordObjectHelper.updateToDatabaseHelper(stmt);
            String p = "UPDATE users SET location = ST_POINT(longitude, latitude);";
            PreparedStatement statementp = ObeoConnection.getInstance().prepareStatement(p);
            RecordObjectHelper.updateToDatabaseHelper(statementp);
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

    private static boolean setStmtMembers(PreparedStatement stmt, ObeoUser obeoUser) {
        try {
            stmt.setString(1, obeoUser.getUser_name());
            stmt.setString(2, obeoUser.getFirst_name());
            stmt.setString(3, obeoUser.getSecond_name());
            stmt.setDate(4, obeoUser.getDate_of_birth());
            stmt.setString(5, obeoUser.getHome_city());
            stmt.setDouble(6, obeoUser.getLongitude());
            stmt.setDouble(7, obeoUser.getLatitude());
            stmt.setString(8, obeoUser.getUser_password());
            stmt.setBoolean(9, obeoUser.isImage_exists());
            stmt.setBoolean(10, obeoUser.isMale());
            stmt.setString(11, obeoUser.getBio());
            stmt.setString(12, obeoUser.getMessenger_token());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static List<MatchedObeoUser> findTourists(ObeoUser obeoUser) {
        try {
            List<ObeoHoliday> holidays = new ArrayList<>();
            ObeoUser user = ObeoUserBuilder.buildUserFromId(obeoUser.getId());




            String s = "SELECT t1.holiday_id FROM holidays AS t1 " +
                    "INNER JOIN(SELECT user_id, MAX(holiday_id) AS maxholiday FROM holidays where " +
                    "destination_city = \'" + user.getHome_city() + "\' GROUP BY user_id) " +
                    "AS t2  ON t1.user_id = t2.user_id AND t1.holiday_id = t2.maxholiday;";







            Statement stmt = ObeoConnection.getInstance().createStatement();
            ResultSet rs = stmt.executeQuery(s);
            while (rs.next()) {
                int i = rs.getInt(1);
                ObeoHoliday holiday = ObeoHolidayBuilder.buildHolidayFromId(rs.getInt(1));

                if (holiday != null) {

                    holidays.add(holiday);
                }
            }

            return touristHelper(obeoUser, holidays);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<MatchedObeoUser> touristHelper(ObeoUser obeoUser, List<ObeoHoliday> holidays) {
        List<MatchedObeoUser> matchedUsers = new ArrayList<>();

        for (ObeoHoliday h: holidays) {
            ObeoUser anotherUser = ObeoUserBuilder.buildUserFromId(h.getUser_id());
            double distance = ObeoUserBuilder.findDistanceBetweenLocalAndHoliday(obeoUser.getId(), h.getId());
            if (distance == -1) {
                return null;
            }
            MatchedObeoUser matchedObeoUser;
            matchedObeoUser = new MatchedObeoUser(obeoUser, anotherUser, distance,
                                MatchingAlgorithm.getTotalScore(obeoUser, anotherUser, distance));
            assert (matchedObeoUser.getScore() == MatchingAlgorithm
                .getTotalScore(anotherUser, obeoUser, distance));

            if (MatchingAlgorithm.getLanguageScore(anotherUser, obeoUser) > 0
                    && !BlockedMatchBuilder.areTwoUsersBlocked(anotherUser.getId(), obeoUser.getId())
                    && !BlockedMatchBuilder.areTwoUsersMatched(anotherUser.getId(), obeoUser.getId())
                    && !anotherUser.equals(obeoUser)) {


                matchedUsers.add(matchedObeoUser);
            }
        }

        Collections.sort(matchedUsers);
        return matchedUsers;


    }



    public static boolean isValidUser(ObeoUser obeoUser) {
        return true;
    }








}
