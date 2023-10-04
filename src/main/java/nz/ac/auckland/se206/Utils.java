package nz.ac.auckland.se206;

import javafx.scene.image.Image;

public class Utils {

  public static String appendBeforeExtension(String url, String append) {
    int lastDot = url.lastIndexOf(".");
    return url.substring(0, lastDot) + append + url.substring(lastDot);
  }

  public static Image getImageWithSuffix(Image base, String suffix) {
    String url = appendBeforeExtension(base.getUrl(), suffix);
    Image image = new Image(url);

    if (image.isError()) {
      return base; // Default to normal image if image with suffix not found
    }

    return image;
  }
}
