package nz.ac.auckland.se206;

import javafx.scene.image.Image;

public class Utils {

  private static long startTime;

  public static String appendBeforeExtension(String url, String append) {
    int lastDot = url.lastIndexOf(".");
    return url.substring(0, lastDot) + append + url.substring(lastDot);
  }

  public static Image getImageWithSuffix(Image base, String suffix) {
    if (base == null) {
      return new Image("/images/placeholder.png");
    }

    String url = appendBeforeExtension(base.getUrl(), suffix);
    Image image = new Image(url);

    if (image.isError()) {
      return base; // Default to normal image if image with suffix not found
    }

    return image;
  }

  public static void startTimeTest() {
    startTime = System.currentTimeMillis();
  }

  public static void logTimeTest(String prefix, int maxMillis) {
    long timeTaken = System.currentTimeMillis() - startTime;
    String colourToggle = timeTaken > maxMillis ? "\u001B[31m" : "\u001B[32m";
    System.out.println("\u001B[33m" + prefix + ": " + colourToggle + timeTaken + "ms\u001B[0m");
  }
}
