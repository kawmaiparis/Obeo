package ClientServer.ServerResponse;

public class CreateImageResponse extends ServerResponse {

  private String image_url;

  public CreateImageResponse(String image_url) {
    this.image_url = image_url;
  }

  public String getImageUrl() {
    return image_url;
  }
}
