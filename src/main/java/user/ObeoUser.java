package user;



import database.RecordObject;
import holidays_table.ObeoHoliday;
import interests.UserInterest;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import languages.UserLanguage;

public class ObeoUser extends RecordObject {

    private String user_name;
    private String first_name;
    private String second_name;
    private Date date_of_birth;
    private String home_city;
    private double longitude;
    private double latitude;
    private String user_password;
    private boolean isMale;
    private String bio;
    private String messenger_token;

    private long age;
    private boolean image_exists;
    private byte[] profile_bits;

    private List<ObeoHoliday> holidayList;
    private List<UserLanguage> userLanguages;
    private List<UserInterest> userInterests;

    public ObeoUser(long id, String user_name, String first_name, String second_name, Date date_of_birth,
                    String home_city, double longitude, double latitude, String user_password, File profile_picture,
                    List<ObeoHoliday> holidayList, List<UserLanguage> userLanguages, List<UserInterest> userInterests,
                    boolean toHash, boolean isMale, String bio) {
        this (id, user_name, first_name, second_name, date_of_birth, home_city, longitude, latitude, null, user_password,
                holidayList, userLanguages, userInterests, toHash, isMale, bio, null);
        setProfile_bits(profile_picture);
        this.image_exists = (profile_picture != null && profile_picture.exists() && !profile_picture.isDirectory());
    }

    public ObeoUser(long id, String user_name, String first_name, String second_name, Date date_of_birth,
                    String home_city, double longitude, double latitude, byte[] profile_picture, String user_password,
                    List<ObeoHoliday> holidayList, List<UserLanguage> userLanguages, List<UserInterest> userInterests,
                    boolean toHash, boolean isMale, String bio, String messenger_token) {
        super(id, "users", "user_id");
        this.user_name = user_name;
        this.first_name = first_name;
        this.second_name = second_name;
        this.messenger_token = messenger_token;
        if (toHash) {
            this.user_password = generateHash(user_password);
        } else {
            this.user_password = user_password;
        }
        this.date_of_birth = date_of_birth;
        this.home_city = home_city;
        this.longitude = longitude;
        this.latitude = latitude;
        this.image_exists =true;
        this.profile_bits = profile_picture;
        this.holidayList = holidayList;
        this.userLanguages = userLanguages;
        this.userInterests = userInterests;
        this.isMale = isMale;
        this.bio = bio;
    }




    public ObeoUser(String user_name, String first_name, String second_name,
                    Date date_of_birth, String home_city, double longitude, double latitude, String user_password, File profile_picture,
                    boolean isMale, String bio) {
        this(-1, user_name, first_name, second_name, date_of_birth, home_city, longitude, latitude, user_password, profile_picture,
                new ArrayList<ObeoHoliday>(), new ArrayList<UserLanguage>(), new ArrayList<UserInterest>(), true, isMale, bio);
    }

    public ObeoUser(String user_name, String first_name, String second_name,
                    Date date_of_birth, String home_city, double longitude, double latitude, String user_password, File profile_picture,
                    boolean toHash, boolean isMale, String bio) {
        this(-1, user_name, first_name, second_name, date_of_birth, home_city, longitude, latitude, user_password, profile_picture,
                new ArrayList<ObeoHoliday>(), new ArrayList<UserLanguage>(), new ArrayList<UserInterest>(), toHash, isMale, bio);
    }



    public String getUser_name() {
        return user_name;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ObeoUser) {
            return this.user_name.equals(((ObeoUser) obj).user_name) && this.id == ((ObeoUser) obj).id;
        }
        return false;
    }

    public long getAge() {
        return age;
    }

    public boolean isImage_exists() {
        return image_exists;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public String getHome_city() {
        return home_city;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setImage_exists(boolean image_exists) {
        this.image_exists = image_exists;
    }

    public byte[] getProfile_bits() {
        return profile_bits;
    }


    private final static String algorithm = "MD5";
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    // given password & salt, generate hashed password
    public static String generateHash(String user_password){
//        if (salt == null) {
//            System.out.println("hi");
//        }
        try {

            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.reset();
//        digest.update(salt);
            byte[] hash = digest.digest(user_password.getBytes());
            return bytesToStringHex(hash);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // convert hashed bytes to String
    public static String bytesToStringHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    // generate random salt
    public static byte[] createSalt() {
        byte[] bytes = new byte[20];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);
        return bytes;
    }

    private void setProfile_bits(File profile_picture) {
        if (profile_picture != null) {
            this.profile_bits = new byte[(int) profile_picture.length()];
            FileInputStream fileInputStream = null;
            try {

                fileInputStream = new FileInputStream(profile_picture);
                fileInputStream.read(profile_bits);
            } catch (Exception e) {
                System.out.println("Cannot read file into byte array");

            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public List<ObeoHoliday> getHolidayList() {
        return holidayList;
    }

    public List<UserLanguage> getUserLanguages() {
        return userLanguages;
    }

    public List<UserInterest> getUserInterests() {
        return userInterests;
    }

    public boolean isMale() {
        return isMale;
    }

    public String getBio() {
        return bio;
    }

    public String getMessenger_token() {
        return messenger_token;
    }

    public void setMessenger_token(String messenger_token) {
        this.messenger_token = messenger_token;
    }

    public void setProfile_bits(byte[] profile_bits) {
        this.profile_bits = profile_bits;
    }
}
