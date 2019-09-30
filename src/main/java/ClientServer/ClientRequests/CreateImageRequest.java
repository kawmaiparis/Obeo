package ClientServer.ClientRequests;

public class CreateImageRequest extends ClientRequest {

  private String image_url;


  public CreateImageRequest(String image_url) {
    this.image_url = image_url;
  }

  public String getImage_url() { return image_url; }
}
