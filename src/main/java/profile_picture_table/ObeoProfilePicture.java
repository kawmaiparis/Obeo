package profile_picture_table;

import database.RecordObject;

import java.util.ArrayList;
import java.util.List;

public class ObeoProfilePicture extends RecordObject {

  private static List<String> reserved = new ArrayList<>();
  private String image_url;

  ObeoProfilePicture(long id, String image_url) {
    super(id, "profile_picture", "user_id");
    this.image_url = image_url;
  }

  public ObeoProfilePicture(String image_url) {
    this(-1, image_url);
  }


  public static List<String> getReserved() {
    return reserved;
  }

  public String getImage_url() {
    return image_url;
  }
}
